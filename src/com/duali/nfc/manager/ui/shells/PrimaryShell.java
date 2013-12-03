package com.duali.nfc.manager.ui.shells;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.connection.ITagConnection;
import com.duali.nfc.connection.T1TConnection;
import com.duali.nfc.connection.T2TConnection;
import com.duali.nfc.connection.T3TConnection;
import com.duali.nfc.connection.T4TConnection;
import com.duali.nfc.connection.internal.T1TConnectionImpl;
import com.duali.nfc.connection.internal.T2TConnectionImpl;
import com.duali.nfc.connection.internal.T3TConnectionImpl;
import com.duali.nfc.connection.internal.T4TConnectionImpl;
import com.duali.nfc.connection.mifare.MAD1;
import com.duali.nfc.connection.mifare.MifareTagConnection;
import com.duali.nfc.connection.mifare.internal.MifareTagConnectionImpl;
import com.duali.nfc.core.connection.exception.ConnectionException;
import com.duali.nfc.manager.handler.CardListener;
import com.duali.nfc.manager.handler.CardPresenceChecker;
import com.duali.nfc.manager.handler.PCSCReaderListener;
import com.duali.nfc.manager.handler.PCSCReaderUpdator;
import com.duali.nfc.manager.ui.composites.AndroidApplicationRecordComposite;
import com.duali.nfc.manager.ui.composites.HandOverRecordComposite;
import com.duali.nfc.manager.ui.composites.MimeRecordComposite;
import com.duali.nfc.manager.ui.composites.SmartPosterRecordComposite;
import com.duali.nfc.manager.ui.composites.TextRecordComposite;
import com.duali.nfc.manager.ui.composites.UriRecordComposite;
import com.duali.nfc.manager.ui.composites.VcardRecordComposite;
import com.duali.nfc.manager.ui.dialog.AboutDialog;
import com.duali.nfc.manager.ui.dialog.P2PReadDialog;
import com.duali.nfc.manager.ui.dialog.P2PSendDialog;
import com.duali.nfc.manager.ui.dialog.TagFormatDialog;
import com.duali.nfc.manager.ui.dialog.TagInfoDialog;
import com.duali.nfc.manager.ui.dialog.TagIssueDialog;
import com.duali.nfc.manager.ui.dialog.TagRecordListContentProvider;
import com.duali.nfc.manager.ui.dialog.util.TopazUtil;
import com.duali.nfc.manager.ui.utils.AppImages;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.p2p.connection.P2PConnection;
import com.duali.nfc.reader.handler.NFCReaderFactory;
import com.duali.nfc.reader.handler.NFCReaderHandler;
import com.duali.nfc.reader.handler.exception.NFCReaderException;
import com.duali.nfc.tag.MifareClassic;
import com.duali.nfc.tag.Tag;
import com.duali.nfc.tag.Type1Tag;
import com.duali.nfc.tag.Type2Tag;
import com.duali.nfc.tag.Type3Tag;
import com.duali.nfc.tag.Type4Tag;
import com.duali.nfc.tag.utils.ATR_Defs;
import com.duali.utils.Hex;

public class PrimaryShell implements PCSCReaderListener, CardListener {
	private static final Logger LOGGER = Logger.getLogger(PrimaryShell.class);
	private String selectedReaderName;

	protected Shell mainShell;
	private Image imageGradient = null;
	private static PrimaryShell instance;

	private Table table;
	private Text txtUid;
	private Text txtType;
	private Text txtMaxSize;
	private Text txtCurrentSize;
	private Text txtStatus;
	private TableViewer tbvRecord;

	private Button rbtnTag;
	private Button rbtnP2p;
	public PrimaryShell() {
		createContents();
	}

	/**
	 * Retrieves the single instance of MainMenuShell.
	 * @return The instance of MainMenuShell.
	 */
	public static synchronized PrimaryShell getInstance() {
		return instance;
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		//		mainShell = new Shell();
		//		mainShell.setSize(450, 300);
		//		mainShell.setText("SWT Application");

		instance = this;
		mainShell = new Shell(DisplayUtils.CURRENT_DISPLAY, SWT.CLOSE | SWT.MIN);
		mainShell.setText(AppLocale.APP_NAME + " " + AppLocale.VERSION);
		mainShell.setImage(AppImages.IMAGE_TAGMANGER_ICON);


		//		mainShell.setBackground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		mainShell.setSize(815, 600);
		mainShell.setMinimumSize(DisplayUtils.DEFAULT_MIN_WIDTH, DisplayUtils.DEFAULT_MIN_HEIGHT);

		//		mainShell.addListener(SWT.DEFAULT, cardCreationEventListener);
		mainShell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		drawGradientBackgorundImage();

		createMenu();
		createHeader();
		createRecordInput();

		createTagInfo();

		createDeviceInfo();
		createFunctionBtn();

		PCSCReaderUpdator.getInstance().addPCSCReaderHirarchyListener(this);
		PCSCReaderUpdator.getInstance().startReaderUpdator();
	}

	/*private Listener cardCreationEventListener = new Listener() {
		public void handleEvent(Event e)  {
			if (e.type == SWT.Resize) {
				drawGradientBackgorundImage();
			}
		}
	};*/

	private void drawGradientBackgorundImage() {
		Image oldImage = imageGradient;
		Rectangle rect = mainShell.getClientArea();
		imageGradient = new Image(DisplayUtils.CURRENT_DISPLAY, rect.width, rect.height);
		GC gc = new GC(imageGradient);
		try {
			gc.setForeground(DisplayUtils.COLOR_BG_DARK_BLUE);
			gc.setBackground(DisplayUtils.COLOR_BG_LIGHT_BLUE);
			gc.fillGradientRectangle(rect.x, rect.y, rect.width,
					rect.height, true);
		} catch (Exception e) {
		} finally {
			gc.dispose();
		}
		mainShell.setBackgroundImage(imageGradient);
		if (oldImage != null) {
			oldImage.dispose();
		}
	}

	private TabFolder tabFolder;
	private void createRecordInput() {
		Composite compDevice = new Composite(mainShell, SWT.NONE);
		GridData gd_compDevice = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_compDevice.widthHint = 312;
		compDevice.setLayoutData(gd_compDevice);
		compDevice.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		GridLayout gl_compDevice = new GridLayout(2, false);
		gl_compDevice.marginBottom = 5;
		gl_compDevice.marginTop = 5;
		gl_compDevice.marginRight = 10;
		gl_compDevice.marginLeft = 10;
		gl_compDevice.horizontalSpacing = 10;
		gl_compDevice.verticalSpacing = 10;
		compDevice.setLayout(gl_compDevice);

		Label lblDevices = new Label(compDevice, SWT.NONE);
		lblDevices.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDevices.setText("Devices");
		lblDevices.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblDevices.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		cmbPcsc = new Combo(compDevice, SWT.READ_ONLY);		
		cmbPcsc.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if ( cmbPcsc.getSelectionIndex() < 0)
					return;

				String selectedPcsc = cmbPcsc.getItem(cmbPcsc.getSelectionIndex());
				LOGGER.debug("PC/SC Device: " + selectedPcsc);

				if(selectedPcsc == null)
					return;

				selectedReaderName = selectedPcsc;
				if(cardPsncChecker != null) cardPsncChecker.stopCheck();
				try {
					cardPsncChecker = new CardPresenceChecker(selectedReaderName, PrimaryShell.this);
					cardPsncChecker.start();
				} catch (CardException e) {
					System.out.println(e.getMessage());
				}				
			}
		});

		cmbPcsc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbPcsc.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		cmbPcsc.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		Label lblStatus = new Label(compDevice, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStatus.setText("status");
		lblStatus.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblStatus.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		txtStatus = new Text(compDevice, SWT.BORDER);
		txtStatus.setEditable(false);
		txtStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtStatus.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtStatus.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		Composite compTagInfo = new Composite(mainShell, SWT.NONE);

		compTagInfo.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		GridLayout gl_compTagInfo = new GridLayout(2, false);
		gl_compTagInfo.marginRight = 10;
		gl_compTagInfo.marginLeft = 10;
		gl_compTagInfo.marginTop = 60;
		gl_compTagInfo.horizontalSpacing = 5;
		gl_compTagInfo.verticalSpacing = 5;
		compTagInfo.setLayout(gl_compTagInfo);
		GridData gd_compTagInfo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2);
		gd_compTagInfo.heightHint = 274;
		gd_compTagInfo.widthHint = 318;
		compTagInfo.setLayoutData(gd_compTagInfo);

		Label lblUid = new Label(compTagInfo, SWT.NONE);
		lblUid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUid.setText("UID");
		lblUid.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblUid.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		txtUid = new Text(compTagInfo, SWT.BORDER);
		txtUid.setEditable(false);
		GridData gd_txtUid = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtUid.widthHint = 208;
		txtUid.setLayoutData(gd_txtUid);
		txtUid.setText("########");
		txtUid.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtUid.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		Label lblType = new Label(compTagInfo, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("TYPE");
		lblType.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblType.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		txtType = new Text(compTagInfo, SWT.BORDER);
		txtType.setEditable(false);
		txtType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtType.setText("########");
		txtType.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtType.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		Label lblMaxSize = new Label(compTagInfo, SWT.NONE);
		lblMaxSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxSize.setText("MAX SIZE");
		lblMaxSize.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblMaxSize.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		txtMaxSize = new Text(compTagInfo, SWT.BORDER);
		txtMaxSize.setText("########");
		txtMaxSize.setEditable(false);
		txtMaxSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtMaxSize.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtMaxSize.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		Label lblCurrentSize = new Label(compTagInfo, SWT.NONE);
		lblCurrentSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCurrentSize.setText("CURRENT SIZE");
		lblCurrentSize.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblCurrentSize.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		txtCurrentSize = new Text(compTagInfo, SWT.BORDER);
		txtCurrentSize.setText("########");
		txtCurrentSize.setEditable(false);
		txtCurrentSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtCurrentSize.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtCurrentSize.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		new Label(compTagInfo, SWT.NONE);
		new Label(compTagInfo, SWT.NONE);
		new Label(compTagInfo, SWT.NONE);
		new Label(compTagInfo, SWT.NONE);
		
		Label lblReaderVer = new Label(compTagInfo, SWT.NONE);
		lblReaderVer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReaderVer.setText("Reader Version");
		lblReaderVer.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblReaderVer.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtReaderVersion = new Text(compTagInfo, SWT.BORDER);
		txtReaderVersion.setText("########");
		txtReaderVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtReaderVersion.setEditable(false);
		txtReaderVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtReaderVersion.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtReaderVersion.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		Composite compRecordInput = new Composite(mainShell, SWT.NONE);
		compRecordInput.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		compRecordInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		compRecordInput.setLayout(new GridLayout(2, false));

		tabFolder = new TabFolder(compRecordInput, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tabFolder.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		tabFolder.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		TabItem tbtmText = new TabItem(tabFolder, SWT.NONE);
		tbtmText.setText("Text");

		TextRecordComposite composite = new TextRecordComposite(tabFolder, SWT.NONE);
		tbtmText.setControl(composite);

		TabItem tbtmUri = new TabItem(tabFolder, SWT.NONE);
		tbtmUri.setText("URI");

		UriRecordComposite composite_1 = new UriRecordComposite(tabFolder, SWT.NONE);
		tbtmUri.setControl(composite_1);

		TabItem tbtmSp = new TabItem(tabFolder, SWT.NONE);
		tbtmSp.setText("Sp");

		SmartPosterRecordComposite composite_2 = new SmartPosterRecordComposite(tabFolder, SWT.NONE);
		tbtmSp.setControl(composite_2);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);

		TabItem tbtmVcard = new TabItem(tabFolder, SWT.NONE);
		tbtmVcard.setText("vCard");

		ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		tbtmVcard.setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		VcardRecordComposite composite_3 = new VcardRecordComposite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite_3);
		scrolledComposite.setMinSize(composite_3.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// TODO: P2P 구현이 완료된 이후
		/*TabItem tbtmMime = new TabItem(tabFolder, SWT.NONE);
		tbtmMime.setText("MIME");

		MimeRecordComposite composite_4 = new MimeRecordComposite(tabFolder, SWT.NONE);
		tbtmMime.setControl(composite_4);*/

		TabItem tbtmAndroidapplication = new TabItem(tabFolder, SWT.NONE);
		tbtmAndroidapplication.setText("Android Application");

		AndroidApplicationRecordComposite composite_5 = new AndroidApplicationRecordComposite(tabFolder, SWT.NONE);
		tbtmAndroidapplication.setControl(composite_5);
		
		// Handover Record
		/*TabItem tbtmHandover = new TabItem(tabFolder, SWT.NONE);
		tbtmHandover.setText("Handover Record");

		HandOverRecordComposite composite_6 = new HandOverRecordComposite(tabFolder, SWT.NONE);
		tbtmHandover.setControl(composite_6);*/

		tbvRecord = new TableViewer(compRecordInput, SWT.BORDER | SWT.FULL_SELECTION);
		table = tbvRecord.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		table.setLinesVisible(true);
		table.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		table.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		tbvRecord.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent e) {
				StructuredSelection selection = (StructuredSelection) e.getSelection();

				if (selection != null) {
					Object obj = selection.getFirstElement();

					LOGGER.debug(selection);
					if ( obj instanceof Record ) {						
						ArrayList<Record> recordList = (ArrayList<Record>) tbvRecord.getInput();
						recordList.remove(obj);
						tbvRecord.setInput(recordList);

						// TODO: 셀렉션
					}	
				}						
			}
		});
		tbvRecord.setLabelProvider(new com.duali.nfc.manager.ui.shells.TagRecordListLabelProvider());
		tbvRecord.setContentProvider(new TagRecordListContentProvider());
		ArrayList<Record> recordList = new ArrayList<Record>();
		tbvRecord.setInput(recordList);


		Button btnNewButton = new Button(compRecordInput, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem tabItem = tabFolder.getItem(tabFolder.getSelectionIndex());

				Control control = (Control) tabItem.getControl();

				if ( control instanceof TextRecordComposite ) {
					LOGGER.debug("Active Tab: TextRecordComposite");
					((TextRecordComposite) control).addRecord(tbvRecord);


				} else if ( control instanceof UriRecordComposite ) {
					LOGGER.debug("Active Tab: UriRecordComposite");
					((UriRecordComposite) control).addRecord(tbvRecord);

				} else if ( control instanceof SmartPosterRecordComposite ) {
					LOGGER.debug("Active Tab: SmartPosterRecordComposite");
					((SmartPosterRecordComposite) control).addRecord(tbvRecord);


				} else if ( control instanceof MimeRecordComposite ) {
					LOGGER.debug("Active Tab: MimeRecordComposite");
					((MimeRecordComposite) control).addRecord(tbvRecord);

				} else if ( control instanceof AndroidApplicationRecordComposite ) {
					LOGGER.debug("Active Tab: AndroidApplicationRecordComposite");
					((AndroidApplicationRecordComposite) control).addRecord(tbvRecord);

				} else if ( control instanceof HandOverRecordComposite ) {
					LOGGER.debug("Active Tab: HandOverRecordComposite");
					((HandOverRecordComposite) control).addRecord(tbvRecord);

				} else if ( control instanceof ScrolledComposite ) {
					Control[] controls = ((ScrolledComposite) control).getChildren();
					for (Control cont: controls) {
						if ( cont instanceof VcardRecordComposite ) {
							LOGGER.debug("Active Tab: VcardRecordComposite");
							((VcardRecordComposite) cont).addRecord(tbvRecord);

							break;
						}
					}
				}
			}
		});
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 100;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("Add");
		btnNewButton.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		btnNewButton.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
	}

	private void createTagInfo() {

	}

	private Combo cmbPcsc;
	private void createDeviceInfo() {
	}

	private void createFunctionBtn() {
		Composite composite = new Composite(mainShell, SWT.NONE);
		composite.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(1, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite.heightHint = 40;
		composite.setLayoutData(gd_composite);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		composite_1.setSize(64, 64);
		composite_1.setLayout(new GridLayout(1, false));

		Composite composite_4 = new Composite(composite_1, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		composite_4.setLayout(new GridLayout(2, false));
		rbtnTag = new Button(composite_4, SWT.RADIO);
//		rbtnTag.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				cmpOptionP2p.setVisible(false);
//			}
//		});
		rbtnTag.setSize(40, 16);
		rbtnTag.setSelection(true);
		rbtnTag.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		rbtnTag.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		rbtnTag.setText("Tag");

		rbtnP2p = new Button(composite_4, SWT.RADIO);
//		rbtnP2p.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				cmpOptionP2p.setVisible(true);
//			}
//		});
		rbtnP2p.setSize(42, 16);
		rbtnP2p.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		rbtnP2p.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		rbtnP2p.setText("P2P");

		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		composite_2.setLayout(new GridLayout(2, false));

		Button btnReadTag = new Button(composite_2, SWT.NONE);
		btnReadTag.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cardPsncChecker != null) cardPsncChecker.stopCheck();

				LOGGER.debug("Read tags action");//Logging for debug purpose.
				NFCReaderHandler nfcReaderHandler;
				try {
					if(selectedReaderName == null || selectedReaderName.trim().length() <= 0){
						showErrorMessageBox(AppLocale.getText("MSG.SELECT.RDR.CONTN"));	//Please select a reader and continue!
						
						startCardPresenceChecking();
						return;
					}

					String selectedReaderStatus = txtStatus.getText();
					if(selectedReaderStatus == null || selectedReaderStatus.trim().length() <= 0
							|| ! selectedReaderStatus.equalsIgnoreCase(READER_STATUS_CONNECTED)){
						showErrorMessageBox(AppLocale.getText("MSG.SELECT.RDR.NOT.CONTN"));	//The selected reader is not available for connection!
						
						startCardPresenceChecking();
						return;
					}

					nfcReaderHandler = NFCReaderFactory.getInstance().getHandler(selectedReaderName.trim());

					if (rbtnP2p.getSelection()) {
						// selected P2P
						LOGGER.debug("P2P Read..");

						P2PReadDialog p2pDialog = new P2PReadDialog(getShell(), 
								SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);

//						if (rbtnSnep.getSelection()) 
							p2pDialog.open(nfcReaderHandler, P2PConnection.SERVICENAME_SNEP);	
//						else
//							p2pDialog.open(nfcReaderHandler, P2PConnection.SERVICENAME_ANDROID_NPP);

					} else {
						// selected Tag
						LOGGER.debug("Tag Read..");

						TagInfoDialog tagInfoDialog = new TagInfoDialog(getShell(),
								SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
						tagInfoDialog.open(nfcReaderHandler);
					}
				} catch (Exception e1) {

				}			
			}
		});
		GridData gd_btnReadTag = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnReadTag.heightHint = 40;
		gd_btnReadTag.widthHint = 120;
		btnReadTag.setLayoutData(gd_btnReadTag);
		btnReadTag.setText("Read NDEF");
		btnReadTag.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		btnReadTag.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		Button btnIssueTag = new Button(composite_2, SWT.NONE);
		GridData gd_btnIssueTag = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnIssueTag.heightHint = 40;
		gd_btnIssueTag.widthHint = 120;
		btnIssueTag.setLayoutData(gd_btnIssueTag);
		btnIssueTag.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cardPsncChecker != null) cardPsncChecker.stopCheck();

				NFCReaderHandler nfcReaderHandler;
				try {
					LOGGER.debug("Issue tags action");//Logging for debug purpose.
					if(selectedReaderName == null || selectedReaderName.trim().length() <= 0){
						showErrorMessageBox(AppLocale.getText("MSG.SELECT.RDR.CONTN"));	//Please select a reader and continue!
						
						startCardPresenceChecking();						
						return;
					}

					String selectedReaderStatus = txtStatus.getText();
					if(selectedReaderStatus == null || selectedReaderStatus.trim().length() <= 0
							|| !selectedReaderStatus.equalsIgnoreCase(READER_STATUS_CONNECTED)){
						showErrorMessageBox(AppLocale.getText("MSG.SELECT.RDR.NOT.CONTN"));	//The selected reader is not available for connection!
						
						startCardPresenceChecking();
						return;
					}					

					nfcReaderHandler = NFCReaderFactory.getInstance().getHandler(selectedReaderName.trim());

					ArrayList<Record> input = (ArrayList<Record>) tbvRecord.getInput();
					if (input.size() <= 0) {
						showInfoMessageBox(AppLocale.MSG_ISSUE_NO_RECORD);
						
						startCardPresenceChecking();
						return;
					}

					if (rbtnP2p.getSelection()) {
						// selected P2P
						LOGGER.debug("P2P Read..");

						P2PSendDialog p2pSendDialog = new P2PSendDialog(getShell(), 
								SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);

//						if (rbtnSnep.getSelection())
							p2pSendDialog.open(input, nfcReaderHandler, P2PConnection.SERVICENAME_SNEP);
//						else
//							p2pSendDialog.open(input, nfcReaderHandler, P2PConnection.SERVICENAME_ANDROID_NPP);

					} else {
						// selected Tag
						LOGGER.debug("Tag Read..");

						TagIssueDialog tagIssueDialog = new TagIssueDialog(getShell(), 
								SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
						tagIssueDialog.open(input, nfcReaderHandler);
					}
				} catch (Exception e1) {

				}	
			}
		});
		btnIssueTag.setText("Write NDEF");
		btnIssueTag.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		btnIssueTag.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		composite_3.setLayout(new GridLayout(1, false));

		Button btnFormatTag = new Button(composite_3, SWT.NONE);
		btnFormatTag.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if(cardPsncChecker != null) cardPsncChecker.stopCheck();

				LOGGER.debug("Format tags action");//Logging for debug purpose.
				NFCReaderHandler nfcReaderHandler;
				try {
					if(selectedReaderName == null || selectedReaderName.trim().length() <= 0){
						showErrorMessageBox(AppLocale.getText("MSG.SELECT.RDR.CONTN"));	//Please select a reader and continue!
						
						startCardPresenceChecking();
						return;
					}

					String selectedReaderStatus = txtStatus.getText();
					if(selectedReaderStatus == null || selectedReaderStatus.trim().length() <= 0
							|| ! selectedReaderStatus.equalsIgnoreCase(READER_STATUS_CONNECTED)){
						showErrorMessageBox(AppLocale.getText("MSG.SELECT.RDR.NOT.CONTN"));	//The selected reader is not available for connection!
						
						startCardPresenceChecking();
						return;
					}

					nfcReaderHandler = NFCReaderFactory.getInstance().getHandler(selectedReaderName.trim());


					MessageBox mb = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
					mb.setText("WARNING");
					mb.setMessage("All data will be erased." +
					"\nDo you want to format a tag?");
					int ret = mb.open();
					if (ret == SWT.YES) {
						TagFormatDialog tafFormatDialog = new TagFormatDialog(getShell(),
								SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
						tafFormatDialog.open(nfcReaderHandler);
					} 			
				} catch (Exception e1) {

				}			

			}
		});
		GridData gd_btnFormatTag = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFormatTag.heightHint = 40;
		gd_btnFormatTag.widthHint = 140;
		btnFormatTag.setLayoutData(gd_btnFormatTag);
		btnFormatTag.setText("Format Tag");
		btnFormatTag.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		btnFormatTag.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

	}

	private void createMenu(){
		GridLayout gl_mainShell = new GridLayout(2, false);
		gl_mainShell.marginBottom = 5;
		gl_mainShell.marginRight = 5;
		gl_mainShell.marginLeft = 5;
		gl_mainShell.horizontalSpacing = 10;
		gl_mainShell.verticalSpacing = 10;
		mainShell.setLayout(gl_mainShell);

		Menu menuBar  = new Menu(mainShell, SWT.BAR);		
		mainShell.setMenuBar(menuBar);

		// File
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);		
		fileMenuHeader.setText("&File");	

		Menu fileMenu = new Menu(mainShell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);

		// File-Exit
		MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("&Exit");
		fileExitItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				dispose();
				getShell().dispose();
				DisplayUtils.CURRENT_DISPLAY.dispose();
				System.exit(0);
			}

		});

		// Help
		MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		helpMenuHeader.setText("&Help");	  		
		Menu helpMenu = new Menu(getShell(), SWT.DROP_DOWN);
		helpMenuHeader.setMenu(helpMenu);

		// Release Note
		MenuItem releaseItem = new MenuItem(helpMenu, SWT.PUSH);
		releaseItem.setText("&Release Note");		
		releaseItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					// NFC_Manager_User_Guide.pdf
					String guideName = "release.txt";
					File file = new File(guideName);

					if (file.exists()) {
						String filePath = file.getAbsolutePath();				

						LOGGER.info("Filepath: " + filePath);//Logging for information purpose.
						startLinkAction("file:///" + filePath);
					} else {
						showInfoMessageBox("Failed to open to \"" + guideName + "\"");
					}


				} catch (Exception e1) {
					// TODO Auto-generated catch block
					LOGGER.error(e1.getMessage(),e1); //Logging error.
					System.out.println("Error " + e1.getMessage());
				}		
			}			
		});

		// Help-About NFC Manager
		MenuItem helpItem = new MenuItem(helpMenu, SWT.PUSH);
		helpItem.setText("&Help Contents");		
		helpItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					// NFC_Manager_User_Guide.pdf
					String guideName = "NFC_Manager_User_Guide.pdf";
					File file = new File(guideName);

					if (file.exists()) {
						String filePath = file.getAbsolutePath();				

						LOGGER.info("Filepath: " + filePath);//Logging for information purpose.
						startLinkAction("file:///" + filePath);
					} else {
						showInfoMessageBox("Failed to open to \"" + guideName + "\"");
					}


				} catch (Exception e1) {
					// TODO Auto-generated catch block
					LOGGER.error(e1.getMessage(),e1); //Logging error.
					System.out.println("Error " + e1.getMessage());
				}		
			}			
		});

		// Help-About NFC Manager
		MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
		aboutItem.setText("&About NFC Manager");		
		aboutItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AboutDialog aboutDialog = new AboutDialog(getShell());
				aboutDialog.open();
			}			
		});
	}

	private void startLinkAction(String linkContent){

		LOGGER.debug(linkContent);
		String os = System.getProperty("os.name").toLowerCase();
		Runtime rt = Runtime.getRuntime();
		try {
			if (os.indexOf("win") >= 0) {

				// this doesn't support showing urls in the form of
				// "page.html#nameLink"
				rt.exec("rundll32 url.dll,FileProtocolHandler " + linkContent);

			} else if (os.indexOf("mac") >= 0) {

				rt.exec("open " + linkContent);

			} else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {

				// Do a best guess on unix until we get a platform independent
				// way
				// Build a list of browsers to try, in this order.
				String[] browsers = { "epiphany", "firefox", "mozilla",
						"konqueror", "netscape", "opera", "links", "lynx" };

				// Build a command string which looks like
				// "browser1 "url" || browser2 "url" ||..."
				StringBuffer cmd = new StringBuffer();
				for (int i = 0; i < browsers.length; i++)
					cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \""
							+ linkContent + "\" ");

				rt.exec(new String[] { "sh", "-c", cmd.toString() });

			} else {
				return;
			}
		} catch (Exception e) {
			return;
		}
	}
	private void createHeader() {
		Composite compHeader = new Composite(mainShell, SWT.NONE);
		compHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		GridLayout gl_compHeader = new GridLayout(2, false);
		gl_compHeader.marginWidth = 0;
		compHeader.setLayout(gl_compHeader);

		// Logo
		Canvas logoCanvas = new Canvas(compHeader, SWT.NONE);
		GridData gd_logoCanvas = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);

		LOGGER.debug("DUALi Logo size(width:" + AppImages.IMAGE_DUALI_LOGO.getImageData().width +
				", height: " + AppImages.IMAGE_DUALI_LOGO.getImageData().height + ")");
		gd_logoCanvas.heightHint = 70;
		gd_logoCanvas.widthHint = 120;
		logoCanvas.setLayoutData(gd_logoCanvas);
		logoCanvas.setLayout(new GridLayout(1, false));
		logoCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(AppImages.IMAGE_DUALI_LOGO,0,0);
			}
		});

		// Header Label
		Label lblHeader = new Label(compHeader, SWT.NONE);
		lblHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblHeader.setFont(FontUtils.FONT_HELVETICAS_NORMAL_SIZE18);
		lblHeader.setForeground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		lblHeader.setText("NFC Manager");
		lblHeader.setText(AppLocale.APP_NAME);
	}

	/**
	 * <p>
	 * This method can be used for getting the sShell.
	 * </p>
	 * 
	 * @return The sShell.
	 */
	public Shell getShell() {
		return mainShell; // returning sShell.
	}

	/** 
	 * <p>This is the method for .</p>
	 */
	public void dispose() {
		if (imageGradient != null) {
			imageGradient.dispose();
		}
		//		PCSCReaderHirarchyUpdator.getInstance().stopSearchingForReader();
	}

	@Override
	public void readerChanged(final String[] updatedReaderList,
			final CardTerminal[] cardTerminals) {
		//		readerList = updatedReaderList;
		//		PrimaryShell.cardTerminals = cardTerminals;

		DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
			public void run() {
				updateReaderStatus(updatedReaderList, cardTerminals);
			}
		});




	}
	private static CardPresenceChecker cardPsncChecker = null;

	public void startCardPresenceChecking(){
		if(cardPsncChecker != null) cardPsncChecker.stopCheck();
		try {
			cardPsncChecker = new CardPresenceChecker(selectedReaderName, this);
			cardPsncChecker.start();
		} catch (CardException e) {
			System.out.println(e.getMessage());
		}
	}

	private static final String READER_STATUS_CONNECTED = AppLocale.getText("CONCTD");
	private static final String READER_STATUS_DISCONNECTED = AppLocale.getText("DISCONCTD");
	private Text txtReaderVersion;

	protected void showInfoMessageBox(final String message){
		DisplayUtils.CURRENT_DISPLAY.asyncExec(new Runnable() {
			public void run() {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
				mb.setText(AppLocale.getText("INFO"));
				mb.setMessage(message);
				mb.open();
			}
		});
	}

	private void showErrorMessageBox(final String message){
		DisplayUtils.CURRENT_DISPLAY.asyncExec(new Runnable() {
			public void run() {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
				mb.setText(AppLocale.getText("ERR"));
				mb.setMessage(message);
				mb.open();
			}
		});
	}
	private synchronized void updateReaderStatus(String[] readerList, CardTerminal[] cardTerminals){
		boolean readerConnected = false;
		cmbPcsc.removeAll();
		if (readerList == null || readerList.length == 0){
			readerConnected = false;
			txtStatus.setText(READER_STATUS_DISCONNECTED);
		} else {

			LOGGER.debug("Selected reader: " + selectedReaderName);//Logging for debug purpose.
			for (int i = 0; i < readerList.length; i++) {				
				cmbPcsc.add(readerList[i], i);				
			}

			for (int i = 0; i < readerList.length; i++) {
				if(selectedReaderName == null)
					break;
				if(readerList[i].indexOf(selectedReaderName) != -1 || readerList[i].equalsIgnoreCase(selectedReaderName)){
					LOGGER.debug("Reader connected " + selectedReaderName);//Logging for debug purpose.
					cmbPcsc.select(i);
					selectedReaderName = readerList[i];
					readerConnected = true;
				}					
			}

			if (!readerConnected) {
				cmbPcsc.select(0);
				selectedReaderName = readerList[0];
				readerConnected = true;
			}

			txtStatus.setText(readerConnected?READER_STATUS_CONNECTED:READER_STATUS_DISCONNECTED);
		}
	}

	@Override
	public void cardConnected(Card card) {
		ATR atr = card.getATR();
		byte[] historicalBytes = atr.getHistoricalBytes();
		String uid = Hex.toHexString(Arrays.copyOfRange(historicalBytes, 1, historicalBytes.length));

		String type = null;

		byte cardType = historicalBytes[0];
		/*
		 * case 0xF0 or 0x01 : Mifare card
		 * case 0xFD or 0x02 : ISO15693 card
		 * case 0xFC or 0x03 : FeliCa card
		 * case 0xF1 : Topaz, NFC Type 1 Tag
		 * case 0x41 or 0x0A : type A
		 * case 0x42 or 0x0B : type B  
		 */
		if (ATR_Defs.isSupportedType1Tag(cardType)) {
			type = "Type1Tag";
		} else if (ATR_Defs.isSupportedType2Tag(cardType)) {
			byte mifareType = historicalBytes[historicalBytes.length-1];

			switch (mifareType) {
			case 0x30: // Mifare 1k
				uid = Hex.toHexString(Arrays.copyOfRange(historicalBytes, 1, historicalBytes.length-1));
				type = "Mifare 1k";
				break;
			case 0x32: // Mifare 4k
				uid = Hex.toHexString(Arrays.copyOfRange(historicalBytes, 1, historicalBytes.length-1));
				type = "Mifare 4k";	
				break;
			case 0x31: // Mifare Ultralight or my-d or kobio
				type = "Type2Tag";
				break;				
			}			

			/*if(historicalBytes[1] == 0x04 || historicalBytes[1] == 0x05 || historicalBytes[1] == 0x37)
				connection = new T2TConnectionImpl(dualiReader);
			else 
				connection =  new MifareTagConnectionImpl(dualiReader);		*/		
		} else if (ATR_Defs.isSupportedType3Tag(cardType)) {
			type = "Type3Tag";
		} else if (ATR_Defs.isSupportedType4Tag(cardType)) {
			type = "Type4Tag";
		} else if (ATR_Defs.isSupportedNFCBarcode(cardType)) {
			type = "NFC Barcode";
			updateUidText(uid);
			updateTypeText(type);
			return;
		} else {
			type = "Not Support";
			updateUidText(uid);
			updateTypeText(type);
			return;
		}

		/*switch (ATR_Defs.classifyTag(atr)) {
		case ATR_Defs.ATR_TYPE1_START_BYTE:
			type = "Type1Tag";
			break;
		case ATR_Defs.ATR_TYPE2_START_BYTE:
			type = "Type2Tag";
			break;
		case ATR_Defs.ATR_TYPE3_START_BYTE:
			type = "Type3Tag";
			break;
		case ATR_Defs.ATR_TYPE4_START_BYTE:
			type = "Type4Tag";
			break;
		case ATR_Defs.ATR_MIFARE_START_BYTE:
			type = "Mifare";	
			break;
		default:
			type = "Not Support";
			updateUidText(uid);
			updateTypeText(type);
			return;
		}*/
		updateUidText(uid);
		updateTypeText(type);

		// 타입별 tag size 얻어오기-----------------------------------------------------------------
		try {
			NFCReaderHandler nfcReaderHandler = NFCReaderFactory.getInstance().getHandler(selectedReaderName);
			ITagConnection connection = nfcReaderHandler.getTagConnection();

			if (connection != null) {
				byte[] version = connection.GetReaderVersion();
				System.out.println("reader version: " + Hex.bytesToHexString(version));
				
				updateReaderVersion(new String(version));
			}
			if ( connection instanceof T1TConnection ) {
				T1TConnection t1tConnection = ((T1TConnection) connection);

				Type1Tag tag = (Type1Tag) t1tConnection.GetTag(Tag.GET_TAG_LEVEL_INFO );
				int memorySize = tag.GetCapabilityContainer().getPhysicalTagMemorySize(Tag.NFC_FORUM_TYPE_1);

				byte lock0 = tag.GetLock0();
				byte lock1 = tag.GetLock1();
				int cntLock0 = TopazUtil.getCountLockBlocks(lock0);
				int cntLock1 = TopazUtil.getCountLockBlocks(lock1);

				int nMax = 0;
				if (memorySize != 0)
					nMax = memorySize - (cntLock0 + cntLock1) * 8 - 6;

				updateMaxSize(nMax+"");

				int currentSize = tag.GetNdefSize();
				updateCurrentSize((currentSize + 2)+"");

			} else if ( connection instanceof T2TConnection) {
				T2TConnection t2tConnection = ((T2TConnection) connection);

				Type2Tag tag = (Type2Tag) t2tConnection.GetTag(Tag.GET_TAG_LEVEL_INFO );
				
//				//세부 Type 2 Tag Infor
//				switch (tag.GetType()) {
//				case Type2Tag.NXP_TYPE_MIFARE_ULTRALIGHT: 
//					type = "Mifare Ultralight";
//					break;
//				case Type2Tag.NXP_TYPE_MIFARE_ULTRALIGHT_C:
//					type = "Mifare Ultralight C or NTAG203";	
//					break;
//				case Type2Tag.INFINEON_TYPE_SLE_66R01P: 
//					type = "SLE 66R01P";
//					break;	
//				case Type2Tag.INFINEON_TYPE_SLE_66R32P: 
//					type = "SLE 66R32P";
//					break;	
//				case Type2Tag.KOVIO_2Kb: 
//					type = "Kovio 2Kb";
//					break;
//				default :
//					type = "Type2Tag";
//					break;
//				}			
//				
//				updateTypeText(type);
				
				int memorySize = tag.GetCapabilityContainer().getPhysicalTagMemorySize(Tag.NFC_FORUM_TYPE_2);

				if (memorySize > 255) {
					updateMaxSize((memorySize-4) + "");
				} else {
					if (memorySize > 0)
						updateMaxSize((memorySize-2) + "");
				}


				int currentSize = tag.GetNdefSize();

				if (memorySize > 255) {
					updateCurrentSize((currentSize + 4) + "");
				} else {
					updateCurrentSize((currentSize + 2) + "");
				}

			} else if ( connection instanceof T3TConnection) {
				T3TConnection t3tConnection = ((T3TConnection) connection);

				Type3Tag tag = (Type3Tag) t3tConnection.GetTag(Tag.GET_TAG_LEVEL_INFO );
				int maxSize = tag.GetAttributeInformation().getNdefMaxSize();
				updateMaxSize(maxSize+"");

				int currentSize = tag.GetNdefSize();
				updateCurrentSize(currentSize+"");

			} else if ( connection instanceof T4TConnection) {				
				T4TConnection t4tConnection = ((T4TConnection) connection);

				Type4Tag tag = (Type4Tag) t4tConnection.GetTag(Tag.GET_TAG_LEVEL_INFO );
				int maxSize = tag.GetCapabilityContainerType4().getNdefMaxSizeToInt();
				updateMaxSize(maxSize+"");

				if (maxSize != 0)
					updateMaxSize((maxSize-2)+"");
				else
					updateMaxSize(maxSize+"");

				int currentSize = tag.GetNdefSize();
				updateCurrentSize(currentSize+"");

			} else if ( connection instanceof MifareTagConnection) {				
				MifareTagConnection mifareConnection = ((MifareTagConnection) connection);

				MifareClassic tag = (MifareClassic) mifareConnection.GetTag(Tag.GET_TAG_LEVEL_INFO );	

				updateMaxSize(tag.GetMaxSize() + "");

				int currentSize = tag.GetNdefSize();
				updateCurrentSize(currentSize+"");
			}			
		} catch (NFCReaderException e) {
			e.printStackTrace();
		} catch (ConnectionException e) {
			updateTypeText("Not Support");
		}
	}

	@Override
	public void cardRemoved() {
		LOGGER.debug("Card removed");

		updateUidText("########");
		updateTypeText("########");

		updateMaxSize("########");
		updateCurrentSize("########");
		
		updateReaderVersion("########");
	}

	private void updateMaxSize(final String maxSize) {
		DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
			public void run() {
				if (maxSize == null) 
					txtMaxSize.setText("########");
				else
					txtMaxSize.setText(maxSize);
			}
		});
	}
	private void updateCurrentSize(final String currentSize) {
		DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
			public void run() {
				if (currentSize == null) 
					txtCurrentSize.setText("########");
				else
					txtCurrentSize.setText(currentSize);
			}
		});
	}
	private void updateUidText(final String uid) {
		DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
			public void run() {
				if (uid == null) 
					txtUid.setText("########");
				else
					txtUid.setText(uid);
			}
		});
	}

	private void updateTypeText(final String type) {
		DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
			public void run() {
				if (type == null) 
					txtType.setText("Not Support");
				else
					txtType.setText(type);	
			}
		});
	}
	
	private void updateReaderVersion(final String version) {
		DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
			public void run() {
				if (version == null) 
					txtReaderVersion.setText("########");
				else
					txtReaderVersion.setText(version);
			}
		});
	}
}
