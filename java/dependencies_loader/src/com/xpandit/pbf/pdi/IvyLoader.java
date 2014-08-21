/*
 * XP.PBF Project
 *
 * Copyright (C) 2013 Xpand IT.
 *
 * This software is proprietary.
 */
package com.xpandit.pbf.pdi;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.event.IvyEvent;
import org.apache.ivy.core.event.IvyListener;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.settings.IvySettings;
import org.pentaho.di.core.logging.LogChannel;
import org.pentaho.di.core.logging.LogChannelInterface;


/**
 * 
 *  Do the ivy dependencies resolve and download
 *  
 * @author DGPD
 * @since  03-07-2013
 * @author <a href="mailto:david.duque@xpand-it.com">David Duque</a>
 */

public class IvyLoader {

	private LogChannelInterface log = new LogChannel(this);

	private JobEntryIvyLoader jobEntry;
	private String config_ivy;
	private String config_ivysettings;
	private String pbf_home;
	private String fileSeparator;

	public static void main(String [ ] args)
	{
		try {
			new IvyLoader().resolveArtifact();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IvyLoader(JobEntryIvyLoader jobEntryIvyLoader) 
	{

		jobEntry=jobEntryIvyLoader;
		pbf_home = jobEntry.getParentJob().getVariable("PBF_HOME");
		fileSeparator = System.getProperty("file.separator");
		config_ivy = pbf_home + fileSeparator + "ivy.xml";
		config_ivysettings = pbf_home + fileSeparator + "ivysettings.xml";
		
	}
 
	/*
	 * Default constructor to enable the standalone debug
	 */
	public IvyLoader() {
		fileSeparator = System.getProperty("file.separator");
		config_ivy="C:\\Users\\admin\\Documents\\Pentaho_Solution_Builder\\Code\\Plugin\\PBF_HOME\\ivy.xml";
		config_ivysettings="C:\\Users\\admin\\Documents\\Pentaho_Solution_Builder\\Code\\Plugin\\PBF_HOME\\ivysettings.xml";
		pbf_home="C:\\Users\\admin\\Documents\\Pentaho_Solution_Builder\\Code\\Plugin\\PBF_HOME\\";
	}

	public void resolveArtifact() throws Exception {				
		System.out.println("Redirecting console");
		PrintStream console = System.out;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);
		System.setOut(ps);
		System.out.println("Redirected");

		//creates clear ivy settings
		//ivy config files to Load
		IvySettings ivySettings = new IvySettings();
		File ivysettings= new File(config_ivysettings);
		ivySettings.load(ivysettings);
		Ivy ivy = Ivy.newInstance(ivySettings);
	

		File ivyfile = new File(config_ivy);
		String[] confs = new String[]{"default"};
		ResolveOptions resolveOptions = new ResolveOptions().setConfs(confs);

		//init resolve report
		ResolveReport report = ivy.resolve(ivyfile.toURL(), resolveOptions);
		
		delete(pbf_home + fileSeparator + "packages" + fileSeparator + "staging",true);

		ArtifactDownloadReport[] allArtifacts = report.getAllArtifactsReports();
		for (int i=0 ;i<allArtifacts.length;++i)		{
			ModuleRevisionId modRev=allArtifacts[i].getArtifact().getModuleRevisionId();

			//Get the parms of the package
			String org=modRev.getOrganisation();
			String revision=modRev.getRevision();
			String name=modRev.getName();

			//so you can get the jar library
			File jarArtifactFile = report.getAllArtifactsReports()[i].getLocalFile();

			String folder = pbf_home + fileSeparator + "packages" + fileSeparator + 
					"staging" + fileSeparator + org + fileSeparator + name + fileSeparator + revision;
			//location inside framework
			File distFolder= new File(folder);

			//recreate and copy files
			distFolder.mkdirs();

			File dist=new File(folder + fileSeparator + jarArtifactFile.getName());

			dist.createNewFile();
			copyFile(jarArtifactFile,dist);     	
		}
		
		log.logBasic(bos.toString());
		System.setOut(console);
		System.out.println("Restore Output");
	}

	/**
	 * 
	 * Copy the file from the ivy location to the staging location of the framework
	 * 
	 * @param ori File orig from ivy
	 * @param dest File destination on PBF Location
	 */
	private void copyFile(File ori,File dest)
	{
		{    
			InputStream inStream = null;
			OutputStream outStream = null;
			try{

				inStream = new FileInputStream(ori);
				outStream = new FileOutputStream(dest); // for override file content

				byte[] buffer = new byte[1024];

				int length;
				while ((length = inStream.read(buffer)) > 0){
					outStream.write(buffer, 0, length);
				}

				if (inStream != null)inStream.close();
				if (outStream != null)outStream.close();

				System.out.println("File Copied.."+dest.getAbsoluteFile());
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
/**
 * 
 * delete the folders from parent to child
 * 
 * @param filePath parent folder
 * @param recursive is recursive?
 * @return
 */
	public static boolean delete(String filePath, boolean recursive) {
		File file = new File(filePath);
		if (!file.exists()) {
			return true;
		}

		if (!recursive || !file.isDirectory())
			return file.delete();

		String[] list = file.list();
		for (int i = 0; i < list.length; i++) {
			if (!delete(filePath + File.separator + list[i], true))
				return false;
		}

		return file.delete();
	}
}
