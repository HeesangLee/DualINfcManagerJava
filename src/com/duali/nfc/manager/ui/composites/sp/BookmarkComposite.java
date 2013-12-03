package com.duali.nfc.manager.ui.composites.sp;

import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Locale;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.composites.AbstractRecordComposite;
import com.duali.nfc.manager.ui.res.NFCForumRTDResource;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FieldValidator;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.manager.ui.utils.OrderedProperties;
import com.duali.nfc.ndef.records.ActionRecord;
import com.duali.nfc.ndef.records.ActionRecord.Action;
import com.duali.nfc.ndef.records.SmartPosterRecord;
import com.duali.nfc.ndef.records.TextRecord;
import com.duali.nfc.ndef.records.UriRecord;

public class BookmarkComposite extends AbstractRecordComposite {
	private Text txtTitle;
	private Text txtUrl;
	private List listUriIdentifier;
	private CCombo cmbAction;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BookmarkComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);
		
		createBookMarkTitle();
		createUriIdentifierList();
		createUrl();
		createActionType();
		
		setDefaultData();
	}

	private void createBookMarkTitle() {
		Label lblTitle = new Label(this, SWT.NONE);
		lblTitle.setAlignment(SWT.RIGHT);
		lblTitle.setBounds(10, 13, 103, 15);
		lblTitle.setText("Title");
		lblTitle.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblTitle.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtTitle = new Text(this, SWT.BORDER);
		txtTitle.setBounds(127, 10, 250, 21);
		txtTitle.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtTitle.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
	}

	private void createUriIdentifierList() {
		Label lblUriIdentifier = new Label(this, SWT.NONE);
		lblUriIdentifier.setText("URI identifier");
		lblUriIdentifier.setAlignment(SWT.RIGHT);
		lblUriIdentifier.setBounds(10, 45, 103, 15);
		lblUriIdentifier.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblUriIdentifier.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		listUriIdentifier = new List(this, SWT.BORDER | SWT.SINGLE| SWT.SELECTED | SWT.V_SCROLL);
		
		OrderedProperties uRProperties = NFCForumRTDResource.getURIIdentifierCodes();

		Enumeration<Object> enumerator = uRProperties.propertyNames();
		while (enumerator.hasMoreElements()) {
			Object object = (Object) enumerator.nextElement();
			//System.out.println("Key " + object);
			//System.out.println("Value " + uRProperties.get(object));
			listUriIdentifier.add((String)uRProperties.get(object));
			listUriIdentifier.setData((String)uRProperties.get(object), (String)object);
		}
		
		listUriIdentifier.setBounds(127, 45, 250, 75);
		listUriIdentifier.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		listUriIdentifier.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		listUriIdentifier.select(0);
		listUriIdentifier.showSelection();
		
		listUriIdentifier.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				int selectedIndex = listUriIdentifier.getSelectionIndex();
				if(selectedIndex > 0){
					String selectedItem = listUriIdentifier.getItem(selectedIndex);
					txtUrl.setText(selectedItem);
					setSelectedIdLength(selectedItem.length());
				}else{
					txtUrl.setText("");
					setSelectedIdLength(0);
				}				
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				int selectedIndex = listUriIdentifier.getSelectionIndex();
				if(selectedIndex > 0){
					String selectedItem = listUriIdentifier.getItem(selectedIndex);
					txtUrl.setText(selectedItem);
					setSelectedIdLength(selectedItem.length());
				}else{
					txtUrl.setText("");
					setSelectedIdLength(0);
				}				
			}
		});
	}
	
	private void createUrl() {
		Label lblUrl = new Label(this, SWT.NONE);
		lblUrl.setText("URL");
		lblUrl.setAlignment(SWT.RIGHT);
		lblUrl.setBounds(10, 137, 103, 15);
		lblUrl.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblUrl.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtUrl = new Text(this, SWT.BORDER);
		txtUrl.setBounds(127, 134, 250, 21);
		txtUrl.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtUrl.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtUrl.addListener(SWT.Verify, urlVerificationListener);
	}

	private void createActionType() {
		Label lblAction = new Label(this, SWT.NONE);
		lblAction.setText("Action");
		lblAction.setAlignment(SWT.RIGHT);
		lblAction.setBounds(10, 173, 103, 15);
		lblAction.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblAction.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		cmbAction = new CCombo(this, SWT.BORDER);
		cmbAction.setBounds(127, 171, 250, 23);
		cmbAction.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		cmbAction.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		cmbAction.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		cmbAction.setEditable(false);
		
		OrderedProperties actionRecordProperties = NFCForumRTDResource.getSPActionRecordTypes();
		Enumeration<Object> enumerator = actionRecordProperties.propertyNames();
		while (enumerator.hasMoreElements()) {
			Object object = (Object) enumerator.nextElement();
			cmbAction.add((String)actionRecordProperties.get(object));
			cmbAction.setData((String)actionRecordProperties.get(object), (String)object);
		}
		
		cmbAction.setText(cmbAction.getItem(0));
	}

	private Listener urlVerificationListener = new Listener() {
		@Override
		public void handleEvent(Event e) {
			if((getSelectedIdLength() > 0)){
				if((e.character == SWT.DEL) && txtUrl.getSelectionCount() > 0 ){
					e.doit = false;
					return;
				}
				if(( txtUrl.getCaretPosition() < getSelectedIdLength()) && (e.character == SWT.DEL)){
					e.doit = false;
					return;
				}
				if(((txtUrl.getCaretPosition() <= getSelectedIdLength()) && (e.character == SWT.BS))){
					e.doit = false;
					return;
				}
			}
		}
	};
	
	private int selectedIdLength = 0;
	
	protected int getSelectedIdLength() {
		return selectedIdLength; // returning selectedIdLength.
	}

	protected void setSelectedIdLength(int selectedIdLength) {
		this.selectedIdLength = selectedIdLength; // Assigning to this.selectedIdLength.
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void clear() {
		txtTitle.setText("");
		listUriIdentifier.select(0);
		listUriIdentifier.showSelection();
		selectedIdLength = 0;
		txtUrl.setText("");
		cmbAction.select(0);		
	}

	public void setDefaultData() {
		txtTitle.setText("DUALi Homepage");
		listUriIdentifier.select(1);
		listUriIdentifier.showSelection();
		selectedIdLength = 11;  
		txtUrl.setText("http://www.duali.com");
		cmbAction.select(0);
	}

	@Override
	public void addRecord(TableViewer tbvRecord) {
		// validate
		// list¿¡ Ãß°¡
		if(txtTitle.getText() == null 
				|| txtTitle.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TITLE); //All fields are mandatory. Please enter valid title.
			return;
		}
		

		String encodingUrl = getEncodingUrl() ;
		if(encodingUrl == null 
				|| encodingUrl.trim().equals("")){
			showInfoMessageBox(AppLocale.getText("MSG.ALL.FIELDS.MANDTRY") + AppLocale.getText("MSG.ENTR.URL"));
			return;
		}		
		
		int selectedIndex = listUriIdentifier.getSelectionIndex();
//		String selectedItem = listUriIdentifier.getItem(selectedIndex);

		if((selectedIndex > 0 && selectedIndex < 5)){
			if(!FieldValidator.isURLString(txtUrl.getText())){
				showInfoMessageBox(AppLocale.getText("MSG.ENTR.VLD.URL"));
				return;
			}
		}
		
		SmartPosterRecord spRecord = new SmartPosterRecord();
		spRecord.setTitle(new TextRecord(txtTitle.getText(), Charset.forName("UTF-8"), new Locale("en")));
		spRecord.setUri(new UriRecord(txtUrl.getText()));
		
		String selectedAction = cmbAction.getText().trim();
		String actionCode = (String) cmbAction.getData(selectedAction);
		int nActionCode = Integer.parseInt(actionCode);
		
		if (nActionCode == Action.DO_THE_ACTION.getValue()) {
			spRecord.setAction(new ActionRecord(Action.DO_THE_ACTION));
		} else if (nActionCode == Action.SAVE_FOR_LATER.getValue()) {
			spRecord.setAction(new ActionRecord(Action.SAVE_FOR_LATER));
		} else if (nActionCode == Action.OPEN_FOR_EDITING.getValue()) {
			spRecord.setAction(new ActionRecord(Action.OPEN_FOR_EDITING));
		}
		
		addRecord(tbvRecord, spRecord);
	}
	
	private String getEncodingUrl(){
		String textEncoded = txtUrl.getText();
		if(textEncoded != null && textEncoded.trim().length() > 0){
			if(getSelectedIdLength() > 0){
				return textEncoded.trim().substring(getSelectedIdLength());
			}else{
				return textEncoded.trim();
			}
		}
		return null;
	}
}
