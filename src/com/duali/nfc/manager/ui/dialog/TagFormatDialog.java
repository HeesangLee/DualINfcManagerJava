package com.duali.nfc.manager.ui.dialog;

import org.apache.log4j.Logger;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.connection.mifare.MAD1;
import com.duali.nfc.manager.handler.tag.formatter.TagFomatterImpl;
import com.duali.nfc.manager.handler.tag.formatter.TagFormatter;
import com.duali.nfc.manager.handler.tag.formatter.TagFormatterListerner;
import com.duali.nfc.manager.ui.dialog.util.TopazUtil;
import com.duali.nfc.manager.ui.shells.PrimaryShell;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
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

public class TagFormatDialog extends Dialog implements TagFormatterListerner {
	private static final Logger LOGGER =
		Logger.getLogger(TagFormatDialog.class);
	
	protected Object result;
	protected Shell shell;
	private TagFormatter tagFormatter;

	private static final int COLOR_RED = 2;
	private static final int COLOR_BLUE = 3;
	private static final int COLOR_GRAY = 4;

	private Label lblNotify;
	private Text txtIcManufacturer;
	private Text txtIcType;
	private Text txtCardUid;
	private Text txtTagType;
	private Text txtNdefSize;
	private Button btnCancel;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TagFormatDialog(Shell parent, int style) {
		super(parent, style);
		setText("Tag Format");
	}

	/**
	 * Open the dialog.
	 * @param input 
	 * @param nfcReaderHandler 
	 * @return the result
	 */
	public Object open(NFCReaderHandler nfcHandler) {
		createContents();
		startTagFormat(nfcHandler);		

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

	private void startTagFormat(NFCReaderHandler nfcReaderHandler) {
		this.tagFormatter = (TagFormatter) TagFomatterImpl.getInstance();
		this.tagFormatter.startTagFormatProcess(this, nfcReaderHandler);            
	}

	private void stopTagFormat() {
		LOGGER.debug("Stoping Card Reading.");
		
		if ( tagFormatter != null) {
			tagFormatter.finishTagFormatProcess();	
		}
	}
	/**
	 * Create contents of the dialog.
	 * @param outputData 
	 */
	private void createContents() {

		shell = new Shell(getParent(), getStyle());
		shell.setBackground(DisplayUtils.COLOR_TAGMAN_BG_LIGHT_GRAY);
		//		shell.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW ));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setSize(656, 179);
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
		txtIcManufacturer.setText("#####");
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
		txtIcType.setText("#####");
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
		txtCardUid.setText("#####");
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
		txtTagType.setText("#####");
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
		txtNdefSize.setText("#####");
		txtNdefSize.setEditable(false);
		txtNdefSize.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtNdefSize.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtNdefSize.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtNdefSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	
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
		stopTagFormat();

		if (imageGradient != null) {
			imageGradient.dispose();
		}
		
//		PrimaryShell.getInstance().updateCardID("########");
//		PrimaryShell.getInstance().updateCardTYPE("########");
		PrimaryShell.getInstance().startCardPresenceChecking();
		shell.dispose();
	}
	
	
	
	@Override
	public void unsupportedTagDetected() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				enableCancelButton();
				updateStatusText("Not Support", COLOR_RED);	
			}
		});
		
		
	}

	@Override
	public void tagDetected(final Tag tag) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				// ListView 
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
					
					ndefSize = nMax + "";

					byte manufactureByte = topazTag.getManufactureByte();
					
					if (manufactureByte == (byte) 0x25) {					
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
				} else if (tag instanceof MifareClassic) {
					MifareClassic mifare1k = (MifareClassic) tag;
					icManufacturer = "NXP Semiconductors";
					icType = "MIFARE Classic";	
					tagType = "MIFARE";
					
					txtCardUid.setText(cardUid);
					txtIcManufacturer.setText(icManufacturer);
					txtIcType.setText(icType);
					txtTagType.setText(tagType);
					txtNdefSize.setText(44 + "");
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
					
					ndefSize = "" + memorySize;

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
						ndefSize = aib.getNdefMaxSize() + "";
					} 
					
					icManufacturer = "Sony Corporation";
					icType = "FeliCa";	
					txtCardUid.setText(cardUid);
					txtIcManufacturer.setText(icManufacturer);
					txtIcType.setText(icType);
					txtNdefSize.setText(ndefSize);
					txtTagType.setText(tagType);
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
							ndefSize = "" + (nMaxSize - 2);
						else
							ndefSize = "" + nMaxSize;
						
						txtNdefSize.setText(ndefSize);
						
						
//						if (nCcLen == 0) {
//							updateNonRecordComposite("CC File No Formatted");
//							enableCancelButton();
//							updateStatusText("Success! Please remove card", COLOR_BLUE);
////							PrimaryShell.getInstance().updateStatusText(AppLocale.getText("CMPLTD"));
//							return;
//						}
					} else {
//						ndefSize = "?/?"; 
//						txtNdefSize.setText(ndefSize);
//						updateNonRecordComposite("CC File NOT FOUND");
//						enableCancelButton();
//						updateStatusText("Success! Please remove card", COLOR_BLUE);
//						return;
					}
				} else {
				}				

				disableCancelButton();			
				
				updateStatusText(AppLocale.MSG_WAIT_WRITING_TO_CARD, COLOR_GRAY);				
			}
		});
	}
	
	public void updateStatusText(final String text, final int color){

		switch (color) {
		case COLOR_BLUE:
			lblNotify.setForeground(DisplayUtils.COLOR_BLUE);
			break;			
		case COLOR_RED:
			lblNotify.setForeground(DisplayUtils.COLOR_LIGHT_RED);
			break;
		case COLOR_GRAY:
			lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);
			break;
		default:
			lblNotify.setForeground(DisplayUtils.COLOR_DARK_GRAY);
			break;
		}
		lblNotify.setText(text);
	}
	
	private void enableCancelButton(){
		btnCancel.setEnabled(true);
		btnCancel.setFocus();
	}

	private void disableCancelButton() {
		btnCancel.setEnabled(false);		
	}

	@Override
	public void tagFormatFail() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				
				enableCancelButton();
				updateStatusText(AppLocale.MSG_ERR_FORMAT_CARD, COLOR_RED);				
			}
		});
	}

	@Override
	public void tagFormatProcessStarted() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void tagFormatSuccess(Tag tag) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				enableCancelButton();
				updateStatusText(AppLocale.MSG_CARD_CREATED_REMOVE, COLOR_BLUE);
				showInfoMessageBoxAndClose(AppLocale.MSG_SUCCS_CREATED_FINISH);
			}
		});
	}

	private void pauseTagFormat(){
		LOGGER.debug("pauseTagFormat.");//Logging for debug purpose.
		this.tagFormatter.pauseTagFormatProcess();
	}
	
	private void showInfoMessageBoxAndClose(final String message){
		DisplayUtils.CURRENT_DISPLAY.syncExec(new Runnable() {
			public void run() {
				pauseTagFormat();
				MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				mb.setText(AppLocale.getText("INFO"));
				mb.setMessage(message);
				mb.open();
				dispose();
			}
		});
	}
	
	@Override
	public void updateStatusNoCard() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				initCardInfo();
				updateStatusText(AppLocale.MSG_PLACE_MF_CARD, COLOR_GRAY);
//				updateNonRecordComposite( "Reading Data.." );
//				tableViewer.setInput(null);
			}
		});
	}
	
	private void initCardInfo() {
		txtCardUid.setText("#####");
		txtIcManufacturer.setText("#####");
		txtIcType.setText("#####");
		txtNdefSize.setText("#####");
		txtTagType.setText("#####");
	}

}
