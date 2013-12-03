package com.duali.nfc.manager.ui.composites;

import java.util.Enumeration;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.res.NFCForumRTDResource;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FieldValidator;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.manager.ui.utils.OrderedProperties;
import com.duali.nfc.ndef.records.UriRecord;

public class UriRecordComposite extends AbstractRecordComposite {
	private Text txtUrl;
	private List listUriIdentifier;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public UriRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);
		
		createUriIdentifierList();
		createUrl();
		
		setDefaultData();
	}

	private void createUriIdentifierList() {
		Label lblUriIdentifier = new Label(this, SWT.NONE);
		lblUriIdentifier.setText("URI identifier");
		lblUriIdentifier.setAlignment(SWT.RIGHT);
		lblUriIdentifier.setBounds(10, 53, 75, 15);
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
		
		listUriIdentifier.setBounds(100, 50, 250, 75);
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
		lblUrl.setBounds(10, 145, 75, 15);
		lblUrl.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblUrl.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		txtUrl = new Text(this, SWT.BORDER);
		txtUrl.setBounds(100, 142, 250, 21);
		txtUrl.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtUrl.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtUrl.addListener(SWT.Verify, urlVerificationListener);
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
	
	private int getSelectedIdLength() {
		return selectedIdLength; // returning selectedIdLength.
	}

	private void setSelectedIdLength(int selectedIdLength) {
		this.selectedIdLength = selectedIdLength; // Assigning to this.selectedIdLength.
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void addRecord(TableViewer tbvRecord) {
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
		
		UriRecord uriRecord = new UriRecord(txtUrl.getText());
		addRecord(tbvRecord, uriRecord);
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

	@Override
	public void clear() {
		listUriIdentifier.select(0);
		listUriIdentifier.showSelection();
		selectedIdLength = 0;
		txtUrl.setText("");
	}

	@Override
	public void setDefaultData() {
		listUriIdentifier.select(1);
		listUriIdentifier.showSelection();
		selectedIdLength = 11;  
		txtUrl.setText("http://www.duali.com");
		
	}
}
