package com.duali.nfc.manager.ui.dialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.duali.nfc.manager.handler.p2p.writer.P2PWriter;
import com.duali.nfc.manager.handler.p2p.writer.P2PWriterImpl;
import com.duali.nfc.manager.handler.p2p.writer.P2PWriterListerner;
import com.duali.nfc.manager.ui.dialog.composites.AndroidApplicationRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.HandOverRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.NonRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.RecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.SmartPosterRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.TextRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.UriRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.VCardRecordComposite;
import com.duali.nfc.manager.ui.dialog.util.CompositeUtil;
import com.duali.nfc.manager.ui.shells.PrimaryShell;
import com.duali.nfc.manager.ui.shells.TagRecordListLabelProvider;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.records.AndroidApplicationRecord;
import com.duali.nfc.ndef.records.BluetoothOOBDataRecord;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.ndef.records.SmartPosterRecord;
import com.duali.nfc.ndef.records.TextRecord;
import com.duali.nfc.ndef.records.UnknownRecord;
import com.duali.nfc.ndef.records.UriRecord;
import com.duali.nfc.ndef.records.VCardRecord;
import com.duali.nfc.reader.handler.NFCReaderHandler;

public class P2PSendDialog extends Dialog implements P2PWriterListerner {
	private static final Logger LOGGER =
		Logger.getLogger(P2PSendDialog.class);

	protected Object result;
	protected Shell shell;
	private P2PWriter p2pWriter;

	private static final int COLOR_RED = 2;
	private static final int COLOR_BLUE = 3;
	private static final int COLOR_GRAY = 4;

	private Label lblNotify;
	private TableViewer  tableViewer;
	private Button btnCancel;
	private Composite recordContainerComposite;
	private ScrolledComposite scrolledComposite;
	private RecordComposite recordComposite;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public P2PSendDialog(Shell parent, int style) {
		super(parent, style);
		setText("P2P Send");
	}

	/**
	 * Open the dialog.
	 * @param input 
	 * @param nfcReaderHandler 
	 * @return the result
	 */
	public Object open(ArrayList<Record> outputData, NFCReaderHandler nfcHandler, String p2pType) {
		createContents(outputData);
		startTagIssue(outputData, nfcHandler, p2pType);		

		shell.addListener(SWT.Resize, cardCreationEventListener);

		//------------------------------------------------
		Rectangle bounds = getParent().getBounds ();
		Rectangle rect = shell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);

		Composite composite = new Composite(shell, SWT.NONE);
		//		composite.setBackground(DisplayUtils.COLOR_TAGMAN_BG_LIGHT_GRAY);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));

		btnCancel = new Button(composite, SWT.NONE);
		GridData gd_btnCancel = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 121;
		btnCancel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
			}
		});
		btnCancel.setText("Cancel");
		//------------------------------------------------

		//------------------------------------------------
		shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.TRAVERSE_ESCAPE)
					event.doit = false;
				// dispose();
			}
		});
		//------------------------------------------------

		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void startTagIssue(ArrayList<Record> outputData, NFCReaderHandler nfcReaderHandler, String p2pType) {
		this.p2pWriter = P2PWriterImpl.getInstance();
		this.p2pWriter.startNDEFWriteProcess(outputData, this, nfcReaderHandler, p2pType);            
	}

	private void stopTagIssue() {
		LOGGER.debug("Stoping Card Reading.");

		if ( p2pWriter != null) {
			p2pWriter.finishNDEFCreationProcess();	
		}
	}
	/**
	 * Create contents of the dialog.
	 * @param outputData 
	 */
	private void createContents(ArrayList<Record> outputData) {

		shell = new Shell(getParent(), getStyle());
		shell.setBackground(DisplayUtils.COLOR_TAGMAN_BG_LIGHT_GRAY);
		//		shell.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW ));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setSize(656, 489);
		shell.setText(getText());
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 0;
		gl_shell.marginTop = 5;
		gl_shell.marginRight = 5;
		gl_shell.marginLeft = 5;
		shell.setLayout(gl_shell);

		Composite composite_1_1 = new Composite(shell, SWT.NONE);
		composite_1_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_1_1.setLayout(new GridLayout(1, false));

		lblNotify = new Label(composite_1_1, SWT.NONE);
		lblNotify.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblNotify.setFont(FontUtils.FONT_TAHOMA_BOLD);
		lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);

		lblNotify.setText(AppLocale.MSG_PLACE_P2P_DEVICE);

		recordContainerComposite = new Composite(shell, SWT.NONE);
		//		recordContainerComposite.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW ));
		//		recordContainerComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		recordContainerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		recordContainerComposite.setLayout(new GridLayout(2, false));

		tableViewer = new TableViewer(recordContainerComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = tableViewer.getTable();
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				System.out.println("widgetDefaultSelected");
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("widgetSelected");
			}
		});
		GridData gd_table = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
		gd_table.widthHint = 200;
		tableViewer.setUseHashlookup(true);
		table.setLayoutData(gd_table);
		table.setLinesVisible(true);
		tableViewer.setLabelProvider(new TagRecordListLabelProvider());
		tableViewer.setContentProvider(new TagRecordListContentProvider());

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
				Object selection = structuredSelection.getFirstElement();

				if (selection instanceof TextRecord) 
					updateComposite(TextRecordComposite.class.getName(), (Record) selection);
				else if (selection instanceof UriRecord) 
					updateComposite(UriRecordComposite.class.getName(), (Record) selection);
				else if (selection instanceof SmartPosterRecord) 
					updateComposite(SmartPosterRecordComposite.class.getName(), (Record) selection);
				else if (selection instanceof VCardRecord) 
					updateComposite(VCardRecordComposite.class.getName(), (Record) selection);
				else if (selection instanceof AndroidApplicationRecord) 
					updateComposite(AndroidApplicationRecordComposite.class.getName(), (Record) selection);
				else if (selection instanceof BluetoothOOBDataRecord) 
					updateComposite(HandOverRecordComposite.class.getName(), (Record) selection);
				else if (selection instanceof UnknownRecord) 
					updateComposite(NonRecordComposite.class.getName(), (Record) selection);
				//				System.out.println(selection);
			}
		});
		//		GridData gd_listRecords = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		//		gd_listRecords.widthHint = 190;		

		Composite composite = new Composite(recordContainerComposite, SWT.NONE);
		composite.setBackgroundMode(SWT.INHERIT_FORCE | SWT.NO_BACKGROUND);
		
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		scrolledComposite = new ScrolledComposite(composite, SWT.NONE | 
				SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setLayout(new GridLayout(1, false));
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		recordComposite = new NonRecordComposite(scrolledComposite, SWT.NONE);
		recordComposite.setLayout(new GridLayout(1, false));
		recordComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		scrolledComposite.setContent(recordComposite);
		scrolledComposite.setMinSize(recordComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		tableViewer.setInput(outputData);
		if ( outputData.size() > 0) {
			Record elements = outputData.get(0);
			tableViewer.setSelection(new StructuredSelection(elements), true);
		}
	}

	private void updateComposite(String className, Record selection) {	
		if (SmartPosterRecordComposite.class.getName().equals(className)) {

		}
		if (recordComposite != null && !recordComposite.isDisposed()) {
			if ( className.equals(CompositeUtil.getClassName(recordComposite)) ) {
				recordComposite.initData(selection);
				return;
			}

			recordComposite.dispose();			
		}

		try {
			Class cls = Class.forName(className);		

			Class[] paramTypes = {
					Composite.class,              
					Integer.TYPE };

			Constructor cons = cls.getConstructor(paramTypes);

			Object[] args = { scrolledComposite, SWT.NONE };
			recordComposite = (RecordComposite) cons.newInstance(args);
			recordComposite.setLayout(new GridLayout(1, false));
			recordComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			recordComposite.initData(selection);
			
			scrolledComposite.setContent(recordComposite);
			scrolledComposite.setMinSize(recordComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			scrolledComposite.layout(true);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private Image imageGradient = null;

	private Listener cardCreationEventListener = new Listener() {
		public void handleEvent(Event e)  {
			if (e.type == SWT.Resize) {
				drawGradientBackgorundImage();
			}
		}
	};

	private void drawGradientBackgorundImage() {
		Image oldImage = imageGradient;
		Rectangle rect = shell.getClientArea();
		imageGradient = new Image(DisplayUtils.CURRENT_DISPLAY, rect.width, rect.height);
		GC gc = new GC(imageGradient);
		try {
			gc.setForeground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
			gc.setBackground(DisplayUtils.COLOR_BG_LIGHT_BLUE);
			gc.fillGradientRectangle(rect.x, rect.y, rect.width,
					rect.height, true);
		} catch (Exception e) {
		} finally {
			gc.dispose();
		}
		shell.setBackgroundImage(imageGradient);
		if (oldImage != null) {
			oldImage.dispose();
		}
	}

	public void dispose() {
		stopTagIssue();

		if (imageGradient != null) {
			imageGradient.dispose();
		}

		//		PrimaryShell.getInstance().updateCardID("########");
		//		PrimaryShell.getInstance().updateCardTYPE("########");
		PrimaryShell.getInstance().startCardPresenceChecking();
		shell.dispose();
	}

	@Override
	public void ndefCreationProcessStarted() {
		// TODO Auto-generated method stub

	}

	
	

	StatusProgress status;
	public synchronized void startStatusProcess(String text) {
		if (status != null) {
			try {
				status.stopRunning();
				status.interrupt();
			} catch (Exception e) {
			}
		}
		status = new StatusProgress(text);
		status.start();
	}

	public void finishStatusProcess() {
		if (status != null) {
			try {
				status.stopRunning();
				status.interrupt();
			} catch (Exception e) {
			}
		}
	}

	class StatusProgress extends Thread {
		private boolean isRunning = false;
		private boolean isRed = false;
		private String text;
		public void stopRunning() {
			this.isRunning = false; // Assigning to this.isRunning.
		}


		public boolean isRunning() {
			return isRunning; // returning isRunning.
		}

		public StatusProgress(String text) {
			this.text = text;
		}

		public void run() {
			isRunning = true;
			while(isRunning) {
				try {
					if (isRed) {
						updateStatusText(text, COLOR_GRAY);
						isRed = false;
					} else {
						updateStatusText(text, COLOR_BLUE);
						isRed = true;
					}		
					Thread.sleep(200);
				} catch (InterruptedException e) {

				}
			}
		}
	}

	public synchronized void updateStatusText(final String text, final int color){

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				switch (color) {
				case COLOR_BLUE:
					if (lblNotify != null && !lblNotify.isDisposed()) {
						lblNotify.setForeground(DisplayUtils.COLOR_BLUE);
					}
					break;			
				case COLOR_RED:
					if (lblNotify != null && !lblNotify.isDisposed()) {
						lblNotify.setForeground(DisplayUtils.COLOR_LIGHT_RED);
					}
					break;
				case COLOR_GRAY:
					if (lblNotify != null && !lblNotify.isDisposed()) {
						lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);
					}
					break;
				default:
					if (lblNotify != null && !lblNotify.isDisposed()) {
						lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);
					}
					break;
				}
				if (lblNotify != null && !lblNotify.isDisposed()) {
					lblNotify.setText(text);
				}
			}
		});
	}

	private void enableCancelButton(){
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if (btnCancel != null && !btnCancel.isDisposed()) {
					btnCancel.setEnabled(true);
					btnCancel.setFocus();
				}				
			}
		});
	}

	private void disableCancelButton() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if (btnCancel != null && !btnCancel.isDisposed()) {
					btnCancel.setEnabled(false);
				}	
			}
		});
	}
	
	private void pauseTagCreation(){
		LOGGER.debug("pauseTagCreation.");//Logging for debug purpose.
		this.p2pWriter.pauseNDEFCreationProcess();
	}

	private void showInfoMessageBoxAndClose(final String message){
		DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
			public void run() {
				pauseTagCreation();
				MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				mb.setText(AppLocale.getText("INFO"));
				mb.setMessage(message);
				mb.open();
				dispose();
			}
		});
	}

	@Override
	public void sendFail(String message) {
		enableCancelButton();
		finishStatusProcess();
		updateStatusText(message, COLOR_RED);				
	}

	@Override
	public void sendSuccess() {
		enableCancelButton();
		finishStatusProcess();
		updateStatusText(AppLocale.MSG_SUCCESS_P2P_FINISH, COLOR_BLUE);
//		showInfoMessageBoxAndClose(AppLocale.MSG_SUCCESS_P2P_FINISH);
	}

	@Override
	public void updateStatusNoDevice() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				updateStatusText(AppLocale.MSG_PLACE_P2P_DEVICE, COLOR_GRAY);
//				updateNonRecordComposite( "Reading Data.." );
				
				if (tableViewer != null && !tableViewer.getControl().isDisposed()) {
//					tableViewer.setInput(null);			
				}
			}
		});
	}

	@Override
	public void sending() {
		disableCancelButton();
		startStatusProcess(AppLocale.MSG_WAIT_WRITING_P2P);
		updateStatusText(AppLocale.MSG_CARD_CREATED_REMOVE, COLOR_BLUE);
	}	
}
