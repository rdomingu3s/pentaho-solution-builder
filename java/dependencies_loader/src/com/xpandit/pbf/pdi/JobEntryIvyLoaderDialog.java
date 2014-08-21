/*
 * XP.PBF Project
 *
 * Copyright (C) 2013 Xpand IT.
 *
 * This software is proprietary.
 */
package com.xpandit.pbf.pdi;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.Const;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entry.JobEntryDialogInterface;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.job.dialog.JobDialog;
import org.pentaho.di.ui.job.entry.JobEntryDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;



/**
 * 
 *  Classe que faz a UI do passo
 *  
 * @author DGPD
 * @since  03-07-2013
 * @author <a href="mailto:david.duque@xpand-it.com">David Duque</a>
 */


public class JobEntryIvyLoaderDialog extends JobEntryDialog implements JobEntryDialogInterface
{
	
	private Button wOK;
	private Listener lsOK;

	private JobEntryIvyLoader     jobEntry;
	private Shell       	shell;
	private PropsUI       	props;
	
	public JobEntryIvyLoaderDialog(Shell parent, JobEntryInterface jobEntryInt, Repository rep, JobMeta jobMeta)
	{
			super(parent, jobEntryInt, rep, jobMeta);
			props=PropsUI.getInstance();
			this.jobEntry=(JobEntryIvyLoader) jobEntryInt;
	
			if (this.jobEntry.getName() == null) this.jobEntry.setName(jobEntryInt.getName());
	}

	public JobEntryInterface open()
	{
		Shell parent = getParent();
		Display display = parent.getDisplay();

        shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);
 		props.setLook(shell);
        JobDialog.setShellImage(shell, jobEntry);

		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText("Do Login ON EAI");
		
		int margin = Const.MARGIN;

		wOK=new Button(shell, SWT.PUSH);
		wOK.setText(" &OK ");

		BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK }, margin,null);


		lsOK       = new Listener() { public void handleEvent(Event e) { ok();     } };
		
		wOK.addListener    (SWT.Selection, lsOK    );
		
        			
		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { ok(); } } );
				
		
		BaseStepDialog.setSize(shell);

		shell.open();
		while (!shell.isDisposed())
		{
		    if (!display.readAndDispatch()) display.sleep();
		}
		return jobEntry;
	}

	public void dispose()
	{
		WindowProperty winprop = new WindowProperty(shell);
		props.setScreen(winprop);
		shell.dispose();
	}
	

	private void ok()
	{
		//jobEntry.setName(wName.getText());
		jobEntry.setChanged(true);
		dispose();
	}

	public String toString()
	{
		return this.getClass().getName();
	}
	
	public boolean evaluates()
	{
		return true;
	}

	public boolean isUnconditional()
	{
		return false;
	}

}
