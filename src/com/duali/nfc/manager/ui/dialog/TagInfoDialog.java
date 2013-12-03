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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.connection.mifare.MAD1;
import com.duali.nfc.manager.handler.tag.reader.TagReader;
import com.duali.nfc.manager.handler.tag.reader.TagReaderImpl;
import com.duali.nfc.manager.handler.tag.reader.TagReaderListerner;
import com.duali.nfc.manager.ui.dialog.composites.AndroidApplicationRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.HandOverRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.MimeRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.NonRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.RecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.SmartPosterRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.TextRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.UriRecordComposite;
import com.duali.nfc.manager.ui.dialog.composites.VCardRecordComposite;
import com.duali.nfc.manager.ui.dialog.util.CompositeUtil;
import com.duali.nfc.manager.ui.dialog.util.TopazUtil;
import com.duali.nfc.manager.ui.shells.PrimaryShell;
import com.duali.nfc.manager.ui.shells.TagRecordListLabelProvider;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.Ndef;
import com.duali.nfc.ndef.NdefRecord;
import com.duali.nfc.ndef.decorder.SmartPosterDecoder;
import com.duali.nfc.ndef.decorder.TextRecordDecoder;
import com.duali.nfc.ndef.decorder.UriRecordDecoder;
import com.duali.nfc.ndef.decorder.VCardRecordDecoder;
import com.duali.nfc.ndef.records.AndroidApplicationRecord;
import com.duali.nfc.ndef.records.BluetoothOOBDataRecord;
import com.duali.nfc.ndef.records.MimeRecord;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.ndef.records.SmartPosterRecord;
import com.duali.nfc.ndef.records.TextRecord;
import com.duali.nfc.ndef.records.UnknownRecord;
import com.duali.nfc.ndef.records.UriRecord;
import com.duali.nfc.ndef.records.VCardRecord;
import com.duali.nfc.reader.handler.NFCReaderHandler;
import com.duali.nfc.tag.MifareClassic;
import com.duali.nfc.tag.Tag;
import com.duali.nfc.tag.Type1Tag;
import com.duali.nfc.tag.Type2Tag;
import com.duali.nfc.tag.Type3Tag;
import com.duali.nfc.tag.Type4Tag;
import com.duali.nfc.tag.attribute.AttributeInformation;
import com.duali.nfc.tag.attribute.CapabilityContainer;
import com.duali.nfc.tag.attribute.CapabilityContainerType4;
import com.duali.nfc.utils.NfcUtils;
import com.duali.utils.Hex;

public class TagInfoDialog extends Dialog implements TagReaderListerner {
	private static final Logger LOGGER =
		Logger.getLogger(TagInfoDialog.class);

	private static final int COLOR_RED = 2;
	private static final int COLOR_BLUE = 3;

	protected Object result;
	protected Shell shell;
	private Label lblNotify;
	private Text txtIcManufacturer;
	private Text txtIcType;
	private Text txtCardUid;
	private Text txtTagType;
	private Text txtNdefSize;
	private TableViewer  tableViewer;

	private Button btnCancel;
//	private Composite recordContainerComposite;
	private ScrolledComposite scrolledComposite;
	private RecordComposite recordComposite;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TagInfoDialog(Shell parent, int style) {
		super(parent, style);
		setText("Tag Read");
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

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open(NFCReaderHandler nfcHandler) {
		createContents();
		startTagCreation(nfcHandler);		

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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
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

		lblNotify.setText(AppLocale.MSG_PLACE_MF_CARD);

		Group grpCardInfo = new Group(shell, SWT.NONE);
		//		grpCardInfo.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW ));
		//		grpCardInfo.setBackgroundMode(SWT.INHERIT_DEFAULT);
		grpCardInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpCardInfo.setText("Card Info");
		GridLayout gl_grpCardInfo = new GridLayout(6, false);
		gl_grpCardInfo.marginRight = 2;
		gl_grpCardInfo.marginLeft = 2;
		grpCardInfo.setLayout(gl_grpCardInfo);

		Label lblNewLabel = new Label(grpCardInfo, SWT.NONE);
		lblNewLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("IC manufacturer");

		txtIcManufacturer = new Text(grpCardInfo, SWT.BORDER);
		txtIcManufacturer.setEditable(false);		
		txtIcManufacturer.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtIcManufacturer.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtIcManufacturer.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));

		txtIcManufacturer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_1 = new Label(grpCardInfo, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_1.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_1.setText("IC Type");

		txtIcType = new Text(grpCardInfo, SWT.BORDER);
		txtIcType.setEditable(false);
		txtIcType.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtIcType.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtIcType.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtIcType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		Label lblNewLabel_2 = new Label(grpCardInfo, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_2.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_2.setText("Card UID");

		txtCardUid = new Text(grpCardInfo, SWT.BORDER);
		txtCardUid.setEditable(false);
		txtCardUid.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtCardUid.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtCardUid.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtCardUid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_3 = new Label(grpCardInfo, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_3.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_3.setText("Tag Type");

		txtTagType = new Text(grpCardInfo, SWT.BORDER);
		txtTagType.setEditable(false);
		txtTagType.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtTagType.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtTagType.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtTagType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_4 = new Label(grpCardInfo, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_4.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_4.setText("NDEF Size");

		txtNdefSize = new Text(grpCardInfo, SWT.BORDER);
		txtNdefSize.setEditable(false);
		txtNdefSize.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtNdefSize.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtNdefSize.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtNdefSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite containerComposite = new Composite(shell, SWT.NONE);
		containerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		containerComposite.setLayout(new GridLayout(2, false));

		tableViewer = new TableViewer(containerComposite, SWT.BORDER | SWT.FULL_SELECTION);
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
				else if (selection instanceof MimeRecord) 
					updateComposite(MimeRecordComposite.class.getName(), (Record) selection);
				else if (selection instanceof AndroidApplicationRecord) 
					updateComposite(AndroidApplicationRecordComposite.class.getName(), (Record) selection);
				else if (selection instanceof BluetoothOOBDataRecord) 
					updateComposite(HandOverRecordComposite.class.getName(), (Record) selection);
				else if (selection instanceof UnknownRecord) 
					updateComposite(NonRecordComposite.class.getName(), (Record) selection);
				//				System.out.println(selection);
			}
		});

		Composite recordContainerComposite = new Composite(containerComposite, SWT.NONE);
		recordContainerComposite.setBackgroundMode(SWT.INHERIT_FORCE | SWT.NO_BACKGROUND);
		
		recordContainerComposite.setLayout(new GridLayout(1, false));
		recordContainerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		scrolledComposite = new ScrolledComposite(recordContainerComposite, SWT.NONE | 
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
		
		
//		recordComposite = new NonRecordComposite(containerComposite, SWT.NONE);
//		recordComposite.setLayout(new GridLayout(1, false));
//		recordComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	public void dispose() {
		stopTagCreation();

		if (imageGradient != null) {
			imageGradient.dispose();
		}
		
//		PrimaryShell.getInstance().updateCardID("########");
//		PrimaryShell.getInstance().updateCardTYPE("########");
		PrimaryShell.getInstance().startCardPresenceChecking();
		shell.dispose();
	}

	private void stopTagCreation() {
		LOGGER.debug("Stoping Card Reading.");
		TagReader ndefTagReader = TagReaderImpl.getInstance();
		ndefTagReader.finishNDEFReadProcess();	
	}

	private void startTagCreation(NFCReaderHandler nfcReaderHandler) {
		TagReader ndefTagReader = TagReaderImpl.getInstance();

		ndefTagReader.startNDEFReadProcess(this, nfcReaderHandler);	
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

	private void updateNonRecordComposite(String data) {
		if (recordComposite != null && !recordComposite.isDisposed()) {
			if ( NonRecordComposite.class.getName().equals(CompositeUtil.getClassName(recordComposite)) ) {
				((NonRecordComposite) recordComposite).initData(data);
				return;
			}

			recordComposite.dispose();			
		}

		try {
			recordComposite = new NonRecordComposite(scrolledComposite, SWT.NONE);

			recordComposite.setLayout(new GridLayout(1, false));
			recordComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			((NonRecordComposite) recordComposite).initData(data);
			scrolledComposite.setContent(recordComposite);
			scrolledComposite.setMinSize(recordComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			scrolledComposite.layout(true);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void ndefReadingProcessStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsupportedTagDetected() {
		if (shell == null)
			return;
		
		if(shell.isDisposed())
			return;
		
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				enableCancelButton();
				updateNonRecordComposite("");
				updateStatusText("Not Support", COLOR_RED);
			}
		});
	}

	@Override
	public void tagDetected(String tagIdHex, String tagType) {


		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				disableCancelButton();				
				updateStatusText("Reading...", COLOR_BLUE);
			}
		});
	}

	@Override
	public void tagRemoved(String tagIdHex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ndeReadingFailed(String tagIdHex, int reason) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				enableCancelButton();
				updateNonRecordComposite("...");
				updateStatusText("Read Failed!", COLOR_BLUE);
			}
		});
	}

	@Override
	public void ndefReadSucceeded(String tagIdHex) {
		LOGGER.debug("card Reading success tagIdHex : " +tagIdHex );

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				enableCancelButton();
				updateStatusText("Success! Please remove card", COLOR_BLUE);
			}
		});
	}

	private void enableCancelButton(){
		btnCancel.setEnabled(true);
		btnCancel.setFocus();
	}

	private void disableCancelButton() {
		btnCancel.setEnabled(false);		
	}

	@Override
	public void ndefReadingProcessFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ndefReadingProcessError(int reason) {
		// TODO Auto-generated method stub

	}

/*	@Override
	public void ndefTagGoingToRead(NdefMessage ndefTag) {
		// TODO Auto-generated method stub

	}*/

	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub

	}

	public void updateStatusText(final String text, final int color){

		switch (color) {
		case COLOR_BLUE:
			lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);
			break;			
		case COLOR_RED:
			lblNotify.setForeground(DisplayUtils.COLOR_LIGHT_RED);
			break;
		default:
			lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);
			break;
		}
		lblNotify.setText(text);
	}

	@Override
	public void updateStatusNoCard() {
//		PrimaryShell.getInstance().updateStatusText(AppLocale.getText("WAIT.CARD"));
		//		updateCardDetail("########", "########");

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				initCardInfo();
				updateStatusText(AppLocale.MSG_PLACE_MF_CARD, COLOR_BLUE);
				updateNonRecordComposite("Reading Data..");
				tableViewer.setInput(null);
			}
		});
	}

	private void initCardInfo() {
		txtCardUid.setText("");
		txtIcManufacturer.setText("");
		txtIcType.setText("");
		txtNdefSize.setText("");
		txtTagType.setText("");
	}

	@Override
	public void updateTag(final Tag tag) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				
				if (txtCardUid == null)
					return;
				if (txtCardUid.isDisposed())
					return;
				// ListView ?…ë°?´íŠ¸
				String cardUid = "";
				String icManufacturer = "";
				String icType = "";
				String ndefSize = "";
				String tagType = "";

				cardUid = Hex.bytesToHexString(tag.GetCardUid());	

				// display card info
				if (tag instanceof Type1Tag) {
					Type1Tag topazTag = (Type1Tag) tag;				

					tagType = "1";
					CapabilityContainer cc = topazTag.GetCapabilityContainer();

					byte[] ndef = topazTag.GetNdef();					
					String strFileSize = "";
					if (ndef != null)
						strFileSize = (ndef.length + 2) + "";
					else
						strFileSize = "0";

					int memorySize = 0;
					if (cc != null)
						memorySize = cc.getPhysicalTagMemorySize(Tag.NFC_FORUM_TYPE_1);
					
					byte lock0 = topazTag.GetLock0();
					byte lock1 = topazTag.GetLock1();
					int cntLock0 = TopazUtil.getCountLockBlocks(lock0);
					int cntLock1 = TopazUtil.getCountLockBlocks(lock1);

					int nMax = 0;
					if (memorySize != 0)
						nMax = memorySize - (cntLock0 + cntLock1) * 8 - 6;
					
					ndefSize = strFileSize + " / " + nMax;

					byte[] uid2 = topazTag.GetUid2();
					
					if (uid2 != null && topazTag.GetUid2()[0] == 0x25) {					
						icManufacturer = "Broadcom Corporation";
						icType = "Topaz IRT5011";
					} else {
						icManufacturer = "Unkown";
						icType = "Unkown";
					}
					
					txtCardUid.setText(cardUid);
					txtIcManufacturer.setText(icManufacturer);
					txtIcType.setText(icType);
					txtNdefSize.setText(ndefSize);
					txtTagType.setText(tagType);
				} else if (tag instanceof Type2Tag) {
					Type2Tag type2Tag = (Type2Tag) tag;	

					tagType = "2";
					CapabilityContainer cc = type2Tag.GetCapabilityContainer();

					int memorySize = 0;
					
					if ( cc != null) {
						memorySize = cc.getPhysicalTagMemorySize(Tag.NFC_FORUM_TYPE_2);
						if (memorySize > 255) {
							memorySize -=4;							
						} else {
							if (memorySize > 0)
								memorySize -=2;
						}
					}
					
					byte[] ndef = tag.GetNdef();					
					String strFileSize = "";
					if (ndef != null) {
						if (memorySize > 255) {
							strFileSize = (ndef.length + 4) + "";
						} else {
							strFileSize = (ndef.length + 2) + "";
						}
					} else {
						strFileSize = "0";
					}
					
					ndefSize = strFileSize + " / " + memorySize;

					switch(type2Tag.GetType()) {
					case Type2Tag.NXP_TYPE_MIFARE_ULTRALIGHT:
						icManufacturer = "NXP";
						icType = "Mifare Ultralight";		
						break;
					case Type2Tag.NXP_TYPE_MIFARE_ULTRALIGHT_C:
						icManufacturer = "NXP";
						icType = "Mifare Ultralight C";		
						break;
					case Type2Tag.NXP_TYPE_NTAG_203:
						icManufacturer = "NXP";
						icType = "NTAG203(F)";		
						break;	
					case Type2Tag.INFINEON_TYPE_SLE_66R01P:
						icManufacturer = "Infinion";
						icType = "SLE 66R01P";	
						break;
					case Type2Tag.INFINEON_TYPE_SLE_66R32P:
						icManufacturer = "Infinion";
						icType = "SLE 66R32P";	
						break;
					case Type2Tag.KOVIO_2Kb:
						icManufacturer = "Kovio";
						icType = "Kovio 2Kb";	
						break;
					}
					
					txtCardUid.setText(cardUid);
					txtIcManufacturer.setText(icManufacturer);
					txtIcType.setText(icType);
					txtNdefSize.setText(ndefSize);
					txtTagType.setText(tagType);
				} else if (tag instanceof Type3Tag) { 
					Type3Tag felicaTag = (Type3Tag) tag;	

					tagType = "3";
					AttributeInformation aib = felicaTag.GetAttributeInformation();					

					if (aib != null) {
						ndefSize = aib.getNdefSize() + "/" +  aib.getNdefMaxSize();
					} 
					
					icManufacturer = "Sony Corporation";
					icType = "FeliCa";	
					txtCardUid.setText(cardUid);
					txtIcManufacturer.setText(icManufacturer);
					txtIcType.setText(icType);
					txtNdefSize.setText(ndefSize);
					txtTagType.setText(tagType);
				} else if (tag instanceof MifareClassic) {
					MifareClassic mifare1k = (MifareClassic) tag;
					icManufacturer = "NXP Semiconductors";
					icType = "MIFARE Classic";	
					tagType = "MIFARE";
					
					txtCardUid.setText(cardUid);
					txtIcManufacturer.setText(icManufacturer);
					txtIcType.setText(icType);
					txtTagType.setText(tagType);
					MAD1 mad1 = mifare1k.GetMAD1();
					
					if (mad1 != null) {
						int maxSize = mifare1k.GetMaxSize();
						int currentSize = tag.GetNdefSize();
						txtNdefSize.setText(currentSize + " / " + maxSize);
					} else {
						
					}
					
				} else if (tag instanceof Type4Tag) { 
					Type4Tag desfire = (Type4Tag) tag;	
					icManufacturer = "NXP Semiconductors";
					icType = "MIFARE DESFire EV1";	
					tagType = "4";

					txtCardUid.setText(cardUid);
					txtIcManufacturer.setText(icManufacturer);
					txtIcType.setText(icType);
					txtTagType.setText(tagType);
					
					CapabilityContainerType4 cc = desfire.GetCapabilityContainerType4();

					if (cc != null) {
						byte[] intValue = new byte[4];
						LOGGER.debug("CC Length: " + Hex.bytesToHexString(cc.getCcLen()));
						intValue[0] = 0x00;
						intValue[1] = 0x00;
						intValue[2] = cc.getCcLen()[0];
						intValue[3] = cc.getCcLen()[1];
						int nCcLen = NfcUtils.bytesToInt(intValue, 0);
						
						LOGGER.debug("NDEF Max Size: " + Hex.bytesToHexString(cc.getNdefMaxSize()));
						intValue[0] = 0x00;
						intValue[1] = 0x00;
						intValue[2] = cc.getNdefMaxSize()[0];
						intValue[3] = cc.getNdefMaxSize()[1];
						int nMaxSize = NfcUtils.bytesToInt(intValue, 0);
						
						byte[] ndef = tag.GetNdef();					
						String strFileSize = "";
						if (ndef != null)
							strFileSize = ndef.length + "";
						else
							strFileSize = "0";
						
						if (nMaxSize != 0)
							ndefSize = strFileSize + " / " + (nMaxSize - 2);
						else
							ndefSize = strFileSize + " / " + nMaxSize;
						
						txtNdefSize.setText(ndefSize);
						
						
						if (nCcLen == 0) {
							updateNonRecordComposite("CC File No Formatted");
							enableCancelButton();
							updateStatusText("Success! Please remove card", COLOR_BLUE);
//							PrimaryShell.getInstance().updateStatusText(AppLocale.getText("CMPLTD"));
							return;
						}
					} else {
						ndefSize = "?/?"; 
						txtNdefSize.setText(ndefSize);
						updateNonRecordComposite("CC File NOT FOUND");
						enableCancelButton();
						updateStatusText("Success! Please remove card", COLOR_BLUE);
//						PrimaryShell.getInstance().updateStatusText(AppLocale.getText("CMPLTD"));
						return;
					}
				} else {
				}

				// display record
				if (tag instanceof Type1Tag ) {
					Type1Tag topazTag  = (Type1Tag) tag;
					if (topazTag.GetCapabilityContainer() == null) {
						updateUnknownTag("Unknown Tag Type");					
						return;
					}

					if (topazTag.GetCapabilityContainer().isNoFormatted()) {
						updateUnknownTag("CC no formatted" + "\n" + topazTag.GetCapabilityContainer().toString());					
						return;
					}

					if (!topazTag.GetCapabilityContainer().isValidNdefMagicNumber()) {
						updateUnknownTag("CC wrong formatted" + "\n" + topazTag.GetCapabilityContainer().toString());					
						return;
					}
				} else if (tag instanceof Type2Tag) {
					Type2Tag type2Tag  = (Type2Tag) tag;
					if (type2Tag.GetCapabilityContainer() == null) {
						updateUnknownTag("Unknown Tag Type");					
						return;
					}

					if (type2Tag.GetCapabilityContainer().isNoFormatted()) {
						updateUnknownTag("CC no formatted" + "\n" + type2Tag.GetCapabilityContainer().toString());					
						return;
					}

					if (!type2Tag.GetCapabilityContainer().isValidNdefMagicNumber()) {
						updateUnknownTag("CC wrong formatted" + "\n" + type2Tag.GetCapabilityContainer().toString());					
						return;
					}

				}  else if (tag instanceof Type3Tag) {
					AttributeInformation aib = ((Type3Tag) tag).GetAttributeInformation();
				} else if (tag instanceof Type4Tag) {
					CapabilityContainerType4 aib = ((Type4Tag) tag).GetCapabilityContainerType4();
				}


				// display record
				if (tag.GetNdef() == null) {
					if (tag instanceof Type4Tag) {
						updateNonRecordComposite("NDEF File NOT FOUND");
						enableCancelButton();
						updateStatusText("Success! Please remove card", COLOR_BLUE);
					} else {
						updateUnknownTag("Unknown Record Type");
					}
					return;
				}

				ArrayList<Record> records = tag.GetRecords();

				updateTagList(records);

				enableCancelButton();
				updateStatusText("Success! Please remove card", COLOR_BLUE);				
			}
		});
	}

	private void updateEmptyTag() {
		LOGGER.debug("updateEmptyTag");

		tableViewer.setInput(null);
		updateNonRecordComposite("Empty Tag");
	}

	private void updateTagList(ArrayList<Record> recordList) {
		LOGGER.debug("updateTagList");		

		tableViewer.setInput(recordList);
		if ( recordList.size() > 0 ) {
//			Table table = tableViewer.getTable();
			
			Record elements = recordList.get(0);
			tableViewer.setSelection(new StructuredSelection(elements), true);
			
		} else {
			updateEmptyTag();
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
}
