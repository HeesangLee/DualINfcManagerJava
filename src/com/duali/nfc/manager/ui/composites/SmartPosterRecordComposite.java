package com.duali.nfc.manager.ui.composites;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.duali.nfc.manager.ui.composites.sp.BookmarkComposite;
import com.duali.nfc.manager.ui.composites.sp.CallRequstComposite;
import com.duali.nfc.manager.ui.composites.sp.SmsComposite;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;

public class SmartPosterRecordComposite extends AbstractRecordComposite {
	public static final int ACTION_BOOKMARK = 2;
	public static final int ACTION_SMS = 3;
	public static final int ACTION_CALL_REQUEST = 4;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SmartPosterRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		Button rbtnBookmark = new Button(this, SWT.RADIO);
		rbtnBookmark.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateUI(ACTION_BOOKMARK);
			}

		});
		rbtnBookmark.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		rbtnBookmark.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		rbtnBookmark.setSelection(true);
		rbtnBookmark.setText("Bookmark");
		
		Button rbtnCallRequest = new Button(this, SWT.RADIO);
		rbtnCallRequest.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		rbtnCallRequest.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		rbtnCallRequest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateUI(ACTION_CALL_REQUEST);
			}
		});
		rbtnCallRequest.setText("Call Request");
		
		Button rbtnSms = new Button(this, SWT.RADIO);
		rbtnSms.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		rbtnSms.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		rbtnSms.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateUI(ACTION_SMS);
			}
		});
		rbtnSms.setText("SMS");
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(null);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		

		createBookmarkComposite(composite);
		createCallRequestComposite(composite);
		createSmsComposite(composite);
	}
	
	private SmsComposite smsComposite;
	private BookmarkComposite bookmarkComposite;
	private CallRequstComposite callRequstComposite;
	private void createSmsComposite(Composite composite) {
		smsComposite = new SmsComposite(composite, SWT.NONE);
		smsComposite.setBounds(0, 0, 400, 250);
		
	}

	private void createCallRequestComposite(Composite composite) {
		callRequstComposite = new CallRequstComposite(composite, SWT.NONE);
		callRequstComposite.setBounds(0, 0, 400, 250);
		
	}

	private void createBookmarkComposite(Composite composite) {
		bookmarkComposite = new BookmarkComposite(composite, SWT.NONE);
		bookmarkComposite.setBounds(0, 0, 400, 250);
	}
	
	private void populateUI(int action) {
		switch (action) {		
		case ACTION_BOOKMARK:

			bookmarkComposite.clear();
			bookmarkComposite.setDefaultData();
			
			bookmarkComposite.setVisible(true);
			smsComposite.setVisible(false);
			callRequstComposite.setVisible(false);

//			currentContentInfoLabel.setText(AppLocale.getText("TITLE.BM.TAG.INFO"));
			break;
		case ACTION_SMS:

			
			smsComposite.clear();
			smsComposite.setDefaultData();
//			cardInfoComposite.clear();

			bookmarkComposite.setVisible(false);
			smsComposite.setVisible(true);
			callRequstComposite.setVisible(false);

//			currentContentInfoLabel.setText(AppLocale.getText("TITLE.SMS.TAG.INFO"));
			break;
		case ACTION_CALL_REQUEST:

			callRequstComposite.clear();
			callRequstComposite.setDefaultData();

			bookmarkComposite.setVisible(false);
			smsComposite.setVisible(false);
			callRequstComposite.setVisible(true);

//			currentContentInfoLabel.setText(AppLocale.getText("TITLE.CALL.TAG.INFO"));
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void addRecord(TableViewer tbvRecord) {
		// 활성호된 컴포지트 찾기
		
		if (bookmarkComposite.isVisible()) {
			bookmarkComposite.addRecord(tbvRecord);
		} else if (callRequstComposite.isVisible()) {
			callRequstComposite.addRecord(tbvRecord);
		} else if (smsComposite.isVisible()) {
			smsComposite.addRecord(tbvRecord);
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultData() {
		// TODO Auto-generated method stub
		
	}
}
