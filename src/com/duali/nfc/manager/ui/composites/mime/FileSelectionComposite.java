package com.duali.nfc.manager.ui.composites.mime;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.composites.AbstractRecordComposite;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.ndef.records.MimeRecord;
import com.duali.utils.FileUtil;

public class FileSelectionComposite extends AbstractRecordComposite {
	public String type;
	private Text txtImagePath;
	private Text txtSize;
	
	public static void main(String[] args) {
		char a = 'F';

		System.out.println((int) a);
	}
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FileSelectionComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);

		create();

		setDefaultData();
	}
	
	

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private void create() {
		txtImagePath = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		txtImagePath.setBounds(37, 21, 224, 20);
		txtImagePath.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtImagePath.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtImagePath.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				String imagePath = txtImagePath.getText();
				File file = new File(imagePath);

				if (file.exists()) {
					long length = file.length();

					NumberFormat formatter = NumberFormat.getNumberInstance();		
					txtSize.setText(formatter.format(length));
				}
			}
		});

		Button btnBrowse = new Button(this, SWT.NONE);
		btnBrowse.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		btnBrowse.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
				fd.setText("Open");
				fd.setFilterPath("C:/");
				String[] filterExt = { "*.*"};
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();

				if ( selected != null) {
					txtImagePath.setText(selected);

					//					txtImagePath.forceFocus();
					txtImagePath.setSelection(selected.length(),selected.length());
				}
			}
		});
		btnBrowse.setBounds(271, 18, 76, 25);
		btnBrowse.setText("Browse");		

		Label lblSize = new Label(this, SWT.NONE);
		lblSize.setBounds(37, 50, 56, 15);
		lblSize.setText("Size");
		lblSize.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblSize.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		txtSize = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.RIGHT);
		txtSize.setBounds(99, 47, 92, 21);
		txtSize.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtSize.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		Label lblBytes = new Label(this, SWT.NONE);
		lblBytes.setBounds(197, 50, 56, 15);
		lblBytes.setText("bytes");
	}	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void clear() {
		txtImagePath.setText("");		
		txtSize.setText("0");
	}

	public void setDefaultData() {
		txtImagePath.setText("");		
		txtSize.setText("0");	
	}

	@Override
	public void addRecord(TableViewer tbvRecord) {
		// validate
		// list¿¡ Ãß°¡
		if(txtImagePath.getText() == null 
				|| txtImagePath.getText().trim().equals("")){
			showInfoMessageBox(AppLocale.MSG_ALL_FIELDS_MANDTRY + AppLocale.MSG_ENTR_IMAGE_PATH); //All fields are mandatory. Please enter valid title.
			return;
		}	

		File file = new File(txtImagePath.getText());

		if (!file.exists()) {
			showErrorMessageBox("File does not exist.");
			return;
		}				

		MimeRecord mimeRecord = new MimeRecord();
		mimeRecord.setType(getType());

		try {
			mimeRecord.setContent(FileUtil.getBytesFromFile(file));
		} catch (IOException e) {
			mimeRecord.setContent(new byte[]{0x00, 0x00});
		}

		addRecord(tbvRecord, mimeRecord);
	}	
}
