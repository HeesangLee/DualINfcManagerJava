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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.duali.nfc.manager.handler.p2p.reader.P2PReader;
import com.duali.nfc.manager.handler.p2p.reader.P2PReaderImpl;
import com.duali.nfc.manager.handler.p2p.reader.P2PReaderListerner;
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
import com.duali.nfc.ndef.Ndef;
import com.duali.nfc.ndef.NdefMessage;
import com.duali.nfc.ndef.NdefMessageDecoder;
import com.duali.nfc.ndef.NdefRecord;
import com.duali.nfc.ndef.decorder.SmartPosterDecoder;
import com.duali.nfc.ndef.decorder.TextRecordDecoder;
import com.duali.nfc.ndef.decorder.UriRecordDecoder;
import com.duali.nfc.ndef.decorder.VCardRecordDecoder;
import com.duali.nfc.ndef.records.AndroidApplicationRecord;
import com.duali.nfc.ndef.records.BluetoothOOBDataRecord;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.ndef.records.SmartPosterRecord;
import com.duali.nfc.ndef.records.TextRecord;
import com.duali.nfc.ndef.records.UnknownRecord;
import com.duali.nfc.ndef.records.UriRecord;
import com.duali.nfc.ndef.records.VCardRecord;
import com.duali.nfc.reader.handler.NFCReaderHandler;
import com.duali.nfc.utils.NfcUtils;

public class P2PReadDialog extends Dialog implements P2PReaderListerner{
	private static final Logger LOGGER =
		Logger.getLogger(P2PReadDialog.class);

	private static final int COLOR_RED = 2;
	private static final int COLOR_BLUE = 3;

	protected Object result;
	protected Shell shlPpRead;
	private Label lblNotify;
	private TableViewer  tableViewer;

	private Button btnCancel;
	private Composite recordContainerComposite;
	private RecordComposite recordComposite;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public P2PReadDialog(Shell parent, int style) {
		super(parent, style);
		setText("NDEF Read");
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
		Rectangle rect = shlPpRead.getClientArea();
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
		shlPpRead.setBackgroundImage(imageGradient);
		if (oldImage != null) {
			oldImage.dispose();
		}
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(NFCReaderHandler nfcHandler, String p2pType) {
		createContents();
		startTagCreation(nfcHandler, p2pType);		

		shlPpRead.addListener(SWT.Resize, cardCreationEventListener);

		//------------------------------------------------
		Rectangle bounds = getParent().getBounds ();
		Rectangle rect = shlPpRead.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shlPpRead.setLocation (x, y);

		Composite composite = new Composite(shlPpRead, SWT.NONE);
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
		shlPpRead.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.TRAVERSE_ESCAPE)
					event.doit = false;
				// dispose();
			}
		});
		//------------------------------------------------

		shlPpRead.open();
		shlPpRead.layout();
		Display display = getParent().getDisplay();
		while (!shlPpRead.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlPpRead = new Shell(getParent(), getStyle());
		shlPpRead.setBackground(DisplayUtils.COLOR_TAGMAN_BG_LIGHT_GRAY);
		//		shell.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW ));
		shlPpRead.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shlPpRead.setSize(656, 489);
		shlPpRead.setText("P2P Read");
		GridLayout gl_shlPpRead = new GridLayout(1, false);
		gl_shlPpRead.verticalSpacing = 0;
		gl_shlPpRead.marginTop = 5;
		gl_shlPpRead.marginRight = 5;
		gl_shlPpRead.marginLeft = 5;
		shlPpRead.setLayout(gl_shlPpRead);

		Composite composite_1_1 = new Composite(shlPpRead, SWT.NONE);
		composite_1_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_1_1.setLayout(new GridLayout(1, false));

		lblNotify = new Label(composite_1_1, SWT.NONE);
		lblNotify.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblNotify.setFont(FontUtils.FONT_TAHOMA_BOLD);
		lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);

		lblNotify.setText(AppLocale.MSG_PLACE_P2P_DEVICE);

		recordContainerComposite = new Composite(shlPpRead, SWT.NONE);
		//		recordContainerComposite.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW ));
		//		recordContainerComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
		recordContainerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		recordContainerComposite.setLayout(new GridLayout(2, false));

		tableViewer = new TableViewer(recordContainerComposite, SWT.BORDER | SWT.FULL_SELECTION);
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

		recordComposite = new NonRecordComposite(recordContainerComposite, SWT.NONE);
		recordComposite.setLayout(new GridLayout(1, false));
		recordComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	public void dispose() {
		stopTagCreation();

		if (imageGradient != null) {
			imageGradient.dispose();
		}

		//		PrimaryShell.getInstance().updateCardID("########");
		//		PrimaryShell.getInstance().updateCardTYPE("########");
		PrimaryShell.getInstance().startCardPresenceChecking();
		shlPpRead.dispose();
	}

	public boolean isAlive() {
		return !shlPpRead.isDisposed();
	}

	private void stopTagCreation() {
		LOGGER.debug("Stoping Card Reading.");
		P2PReader ndefTagReader = P2PReaderImpl.getInstance();
		ndefTagReader.finishNDEFReadProcess();	
	}

	private void startTagCreation(NFCReaderHandler nfcReaderHandler, String p2pType) {
		P2PReader p2pReader = P2PReaderImpl.getInstance();

		p2pReader.startNDEFReadProcess(this, nfcReaderHandler, p2pType);	
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

			Object[] args = { recordContainerComposite, SWT.NONE };
			recordComposite = (RecordComposite) cons.newInstance(args);


			recordComposite.setLayout(new GridLayout(1, false));
			recordComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			recordComposite.initData(selection);
			recordContainerComposite.layout(true);

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

	private void updateNonRecordComposite(String data) {
		if (recordComposite != null && !recordComposite.isDisposed()) {
			if ( NonRecordComposite.class.getName().equals(CompositeUtil.getClassName(recordComposite)) ) {
				((NonRecordComposite) recordComposite).initData(data);
				return;
			}

			recordComposite.dispose();			
		}

		try {
			recordComposite = new NonRecordComposite(recordContainerComposite, SWT.NONE);

			recordComposite.setLayout(new GridLayout(1, false));
			recordComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			((NonRecordComposite) recordComposite).initData(data);
			recordContainerComposite.layout(true);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void ndefReadingProcessStarted() {
		// TODO Auto-generated method stub

	}	

	private void enableCancelButton(){
		if(!btnCancel.isDisposed()) {
			btnCancel.setEnabled(true);
			btnCancel.setFocus();
		}
	}

	private void disableCancelButton() {
		if ( btnCancel != null && !btnCancel.isDisposed())
			btnCancel.setEnabled(false);		
	}

	public void updateStatusText(final String text, final int color){

		switch (color) {
		case COLOR_BLUE:

			if ( lblNotify != null && !lblNotify.isDisposed())
				lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);
			break;			
		case COLOR_RED:
			if ( lblNotify != null && !lblNotify.isDisposed())
				lblNotify.setForeground(DisplayUtils.COLOR_LIGHT_RED);
			break;
		default:
			if ( lblNotify != null && !lblNotify.isDisposed())
				lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);
			break;
		}
		if ( lblNotify != null && !lblNotify.isDisposed())
			lblNotify.setText(text);
	}	

	private void updateEmptyTag() {
		LOGGER.debug("updateEmptyTag");

		if(tableViewer != null && !tableViewer.getControl().isDisposed())
			tableViewer.setInput(null);
		updateNonRecordComposite("Empty Tag");
	}

	private void updateTagList(ArrayList<Record> recordList) {
		LOGGER.debug("updateTagList");		

		if(tableViewer != null && !tableViewer.getControl().isDisposed()) {
			tableViewer.setInput(recordList);
			if ( recordList.size() > 0 ) {
				//			Table table = tableViewer.getTable();

				Record elements = recordList.get(0);
				tableViewer.setSelection(new StructuredSelection(elements), true);

			} else {
				updateEmptyTag();
			}
		}
		LOGGER.error("NDEF Record length: " + recordList.size());		
	}

	private void updateTagList(NdefRecord[] ndefRecords) {
		LOGGER.debug("updateTagList");


		//		LinearLayout linearListTagView = (LinearLayout) findViewById(R.id.linearListTagView);
		//		linearListTagView.removeAllViews();
		//
		//		ListView listTag = new ListView(ITouchNfcDemoActivity.this);
		//		listTag.setId(R.id.listTag);
		//		listTag.setOnItemClickListener(ITouchNfcDemoActivity.this);
		//		linearListTagView.addView(listTag);				
		//

		ArrayList<Record> recordList = new ArrayList<Record>();

		LOGGER.debug("Record Size: " + ndefRecords.length);

		for(NdefRecord ndefRecord: ndefRecords) {
			Record record = null;
			byte[] type = ndefRecord.getType();
			try {
				if( NfcUtils.isEqualArray(type, SmartPosterRecord.TYPE) ) { 
					SmartPosterDecoder decoder = new SmartPosterDecoder();
					if (decoder.canDecode(ndefRecord))
						record = decoder.decodeRecord(ndefRecord, Ndef.getNdefMessageDecoder());

				} else if ( NfcUtils.isEqualArray(type, TextRecord.TYPE) ) {
					TextRecordDecoder decoder = new TextRecordDecoder();
					if (decoder.canDecode(ndefRecord))
						record = decoder.decodeRecord(ndefRecord, Ndef.getNdefMessageDecoder());
				} else if ( NfcUtils.isEqualArray(type, UriRecord.TYPE) ) {
					UriRecordDecoder decoder = new UriRecordDecoder();
					if (decoder.canDecode(ndefRecord))
						record = decoder.decodeRecord(ndefRecord, Ndef.getNdefMessageDecoder());
				} else if ( NfcUtils.isEqualArray(type, VCardRecord.TYPE) ) {
					VCardRecordDecoder decoder = new VCardRecordDecoder();
					if (decoder.canDecode(ndefRecord))
						record = decoder.decodeRecord(ndefRecord, Ndef.getNdefMessageDecoder());
				} else {
					record = new UnknownRecord();	
				}
			} catch (OutOfMemoryError e1) {
				LOGGER.error("OutOfMemoryError");
				record = new UnknownRecord();	
			} catch (RuntimeException e2) {
				LOGGER.error("RuntimeException");
				record = new UnknownRecord();	
			} catch (Exception e3) {
				LOGGER.error("Exception");
				record = new UnknownRecord();	
			} 

			if(record == null)
				record = new UnknownRecord();
			recordList.add(record);
		}

		tableViewer.setInput(recordList);
		if ( recordList.size() > 0 ) {
			Table table = tableViewer.getTable();
			//			table.setRedraw(false);
			//			tableViewer.refresh(true, false);
			//			table.deselectAll();
			Record elements = recordList.get(0);
			tableViewer.setSelection(new StructuredSelection(elements), true);
			//			Table table = tableViewer.getTable();
			//			table.deselectAll();
			//			table.select(0);
			//			
			//			table.setRedraw(true);
		}
		//		listTag.setAdapter(
		//				new TagListAdapter(ITouchNfcDemoActivity.this, R.layout.tag_list_item, recordList));

		LOGGER.error("NDEF Record length: " + ndefRecords.length);
		//		Log.d(TAG, "NDEF Record length: " + ndefRecords.length);


	}

	private void updateUnknownTag(final String string) {
		LOGGER.debug("updateUnknownTag");

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {

				tableViewer.setInput(null);

				updateNonRecordComposite(string);
				enableCancelButton();
				updateStatusText("Success! Please remove card", COLOR_BLUE);
				//				PrimaryShell.getInstance().updateStatusText(AppLocale.getText("CMPLTD"));
			}
		});


	}

	@Override
	public void updateNdef(final byte[] ndef) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				ArrayList<Record> records = getRecords(ndef);
				updateTagList(records);
			}
		});

	}

	private ArrayList<Record> getRecords(byte[] ndef) {
		NdefMessageDecoder ndefMessageDecoder =  Ndef.getNdefMessageDecoder();

		ArrayList<Record> records = new ArrayList<Record>();
		try {
			if (ndef != null) {
				NdefMessage ndefMessage = ndefMessageDecoder.decode(ndef);
				records = (ArrayList<Record>) ndefMessageDecoder.decodeToRecords(ndefMessage);				
			} else {				
				records.add(new UnknownRecord());
				return records;
			}
		} catch (OutOfMemoryError e) {	
			records.add(new UnknownRecord());		
		} catch (RuntimeException e1) {
			records.add(new UnknownRecord());			
		} catch (Exception e2) {
			records.add(new UnknownRecord());		
		}		
		return records;

	}

	@Override
	public void deviceRemoved(String cardUid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStatusNoDevice() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				updateStatusText(AppLocale.MSG_PLACE_P2P_DEVICE, COLOR_BLUE);
				enableCancelButton();
				updateNonRecordComposite( "Reading Data.." );

				if(!tableViewer.getControl().isDisposed())
					tableViewer.setInput(null);
			}
		});

	}

	@Override
	public void readSuccess() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				enableCancelButton();
				updateStatusText("Success! Please remove NFC device", COLOR_BLUE);		
			}
		});		
	}

	@Override
	public void reading() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				disableCancelButton();				
				updateStatusText("Reading...", COLOR_BLUE);		
			}
		});		
	}

	@Override
	public void readFail() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				enableCancelButton();
				updateStatusText("Fail!", COLOR_RED);		
			}
		});	
	}
}