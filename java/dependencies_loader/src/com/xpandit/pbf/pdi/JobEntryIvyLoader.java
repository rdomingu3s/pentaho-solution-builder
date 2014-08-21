/*
 * XP.PBF Project
 *
 * Copyright (C) 2013 Xpand IT.
 *
 * This software is proprietary.
 */
package com.xpandit.pbf.pdi;

import java.util.List;

import org.pentaho.di.cluster.SlaveServer;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.job.entry.JobEntryBase;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.w3c.dom.Node;


/**
 * 
 *  Classe that load and stores the XML of the step
 *  
 * @author DGPD
 * @since  03-07-2013
 * @author <a href="mailto:david.duque@xpand-it.com">David Duque</a>
 */


public class JobEntryIvyLoader extends JobEntryBase implements Cloneable, JobEntryInterface {
	

public JobEntryIvyLoader(String n) {
    super(n, "");
    setID(-1L);
  }

  public JobEntryIvyLoader() {
    this("");
  }

  public Object clone() {
    JobEntryIvyLoader je = (JobEntryIvyLoader) super.clone();
    return je;
  }

  public String getXML() {
    StringBuffer retval = new StringBuffer();

    retval.append(super.getXML());
    
    return retval.toString();
  }
  
  @Override
  public void loadXML(Node entrynode, List<DatabaseMeta> databases, List<SlaveServer> slaveServers, Repository rep) throws KettleXMLException {
    try {
      super.loadXML(entrynode, databases, slaveServers);
    } catch (KettleXMLException xe) {
      throw new KettleXMLException("Unable to load file exists job entry from XML node",
          xe);
    }
  }
  
  @Override
  public void loadRep(Repository rep, ObjectId id_jobentry, List<DatabaseMeta> databases, List<SlaveServer> slaveServers) throws KettleException {
	  try {
      super.loadRep(rep, id_jobentry, databases, slaveServers);
    } catch (KettleException dbe) {
      throw new KettleException(
          "Unable to load job entry for type file exists from the repository for id_jobentry="
              + id_jobentry, dbe);
    }
  }

  public void saveRep(Repository rep, ObjectId id_job) throws KettleException {
    try {
      super.saveRep(rep, id_job);
    } catch (KettleDatabaseException dbe) {
      throw new KettleException(
          "unable to save jobentry of type 'file exists' to the repository for id_job="
              + id_job, dbe);
    }
  }

  public Result execute(Result prev_result, int nr) {
    Result result = new Result(nr);
    result.setResult(false);
    logDetailed(toString(), "Start of processing");

    // String substitution..
    IvyLoader proc = new IvyLoader(this);
   
    try {
    	proc.resolveArtifact();
    	result.setResult(true);
    } catch (Exception e) {
      result.setNrErrors(1);
      e.printStackTrace();
      logError(toString(), "Error processing DummyJob : " + e.getMessage());
    }

    return result;
  }

  public boolean evaluates() {
    return true;
  }
}
