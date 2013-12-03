package com.duali.nfc.manager.ui.composites.sp;

import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Locale;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.composites.AbstractRecordComposite;
import com.duali.nfc.manager.ui.res.NFCForumRTDResource;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.manager.ui.utils.OrderedProperties;
import com.duali.nfc.ndef.records.ActionRecord;
import com.duali.nfc.ndef.records.ActionRecord.Action;
import com.duali.nfc.ndef.records.SmartPosterRecord;
import com.duali.nfc.ndef.records.TextRecord;
import com.duali.nfc.ndef.records.UriRecord;

public class SmsComposite extends AbstractRecordComposite {
	private Text txtTitle;
	private Text txtPhoneNumber;
	private Text txtMessage;
	private CCombo cmbAction;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SmsComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);
		
		createTitle();
		createPhoneNumber();
		createMessageBox();
		createActionType();		
		
		setDefaultData();
	}

	private void createMessageBox() {
		Label lblMessage = new Label(this, SWT.NONE);
		lblMessage.setText("Message");
		lblMessage.setAlignment(SWT.RIGHT);
		lblMessage.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblMessage.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblMessage.setBounds(10, 84, 103, 15);		
		
		txtMessage =  new Text(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		txtMessage.setBounds(127, 80, 250, 75);
		txtMessage.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtMessage.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtMessage.setTextLimit(150);
		txtMessage.addTraverseListener(new TraverseListener() { // adding tab for focus traversal
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
                    e.doit = true;
                }
            }
        });
	}

	private void createTitle() {			
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

	private void createPhoneNumber() {
		Label lblPhoneNumber = new Label(this, SWT.NONE);
		lblPhoneNumber.setText("Phone number");
		lblPhoneNumber.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblPhoneNumber.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblPhoneNumber.setAlignment(SWT.RIGHT);
		lblPhoneNumber.setBounds(10, 48, 103, 15);
		
		txtPhoneNumber = new Text(this, SWT.BORDER);
		txtPhoneNumber.setBounds(127, 45, 250, 21);
		txtPhoneNumber.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtPhoneNumber.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtPhoneNumber.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						if(chars[i] != ')' && chars[i] != '(' && chars[i] != ' ' && chars[i] != '-' && chars[i] != '+')
							e.doit = false;
						return;
					}
				}
			}
		});
	}

	private void createActionType() {
		Label lblAction = new Label(this, SWT.NONE);
		lblAction.setText("Action");
		lblAction.setAlignment(SWT.RIGHT);
		lblAction.setBounds(10, 172, 103, 15);
		lblAction.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblAction.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		cmbAction = new CCombo(this, SWT.BORDER);
		cmbAction.setBounds(127, 169, 241, 23);
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
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void clear() {
        txtTitle.setText("");
        txtPhoneNumber.setText("");
        txtMessage.setText("");
        cmbAction.select(0);
	}

	public void setDefaultData() {
		txtTitle.setText("QnA");
		txtPhoneNumber.setText("+82-31-213-0074");
		txtMessage.setText("There is a problem.");
		cmbAction.select(0);
	}

	public void addRecord(TableViewer tbvRecord) {
		if(txtTitle.getText() == null 
				|| txtTitle.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_TITLE); //All fields are mandatory. Please enter valid title.
			return;
		}
		
		if(txtPhoneNumber.getText() == null 
				|| txtPhoneNumber.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.getText("MSG.ENTER.VALID", new String[]{AppLocale.getText("CALLREQ.FON.NUM")}));
			return;
		}

		if(txtPhoneNumber.getText().length() < 7 ){
			showInfoMessageBox(AppLocale.getText("MSG.TEL.NUM.ATLST"));
			return;
		}
		
		if(txtMessage.getText() == null 
				|| txtMessage.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_MESSAGE); //All fields are mandatory. Please enter valid title.
			return;
		}
		
		SmartPosterRecord spRecord = new SmartPosterRecord();
		spRecord.setTitle(new TextRecord(txtTitle.getText(), Charset.forName("UTF-8"), new Locale("en")));
		spRecord.setUri(new UriRecord("sms:" + txtPhoneNumber.getText() +"?body=" + txtMessage.getText()));
		
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
}
