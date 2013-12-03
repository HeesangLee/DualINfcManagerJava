package com.duali.nfc.manager.ui.composites.mime;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.composites.AbstractRecordComposite;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.manager.ui.utils.ImageUtil;
import com.duali.nfc.ndef.records.MimeRecord;
import com.duali.utils.FileUtil;

public class ImageJpegComposite extends AbstractRecordComposite {
	public static final String TYPE = "image/jpeg";
	private Text txtImagePath;
	private Text txtSize;
	private Canvas canvas;
	
	public static void main(String[] args) {
		char a = 'F';

		System.out.println((int) a);
	}
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ImageJpegComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(null);

		create();

		setDefaultData();
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
				String[] filterExt = { "*.jpg", "*.jpeg"};
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();

				if ( selected != null) {
					txtImagePath.setText(selected);

					//					txtImagePath.forceFocus();
					txtImagePath.setSelection(selected.length(),selected.length());

					canvas.redraw();
				}
			}
		});
		btnBrowse.setBounds(271, 18, 76, 25);
		btnBrowse.setText("Browse");

		canvas = new Canvas(this, SWT.NONE);
		
		final int canvasWidth = 230;
		final int canvasHeight = 120;
		canvas.setBounds(50, 75, canvasWidth, canvasHeight);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				String imagePath = txtImagePath.getText();
				if ( imagePath.trim().length() <= 0  ) {
					Image image = new Image(getDisplay(), canvasWidth, canvasHeight);
			        GC gc = new GC(image);
			        
			        gc.drawRectangle(new Rectangle(0, 0, canvasWidth-1, canvasHeight-1));
			        gc.drawLine(0,0,canvasWidth,canvasHeight);
			        gc.drawLine(canvasWidth,0,0,canvasHeight);
			        gc.dispose();

			        e.gc.drawImage(image, 0, 0);
			        image.dispose();
					
					return;
				}
								
				Image image = new Image(getDisplay(), txtImagePath.getText());
				
				if (image != null) {
					Image scaled = ImageUtil.resize(image, canvasWidth, canvasHeight);
	
					GC gc = new GC(scaled);
			        gc.drawRectangle(new Rectangle(0, 0, scaled.getBounds().width-1, scaled.getBounds().height-1));
			        gc.dispose();
			        
					e.gc.drawImage(scaled, 0, 0);									
					scaled.dispose();
				}				
			}
		});

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
		canvas.redraw();
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
		mimeRecord.setType(TYPE);


		try {
			mimeRecord.setContent(FileUtil.getBytesFromFile(file));
		} catch (IOException e) {
			mimeRecord.setContent(new byte[]{0x00, 0x00});
		}

		addRecord(tbvRecord, mimeRecord);
	}	
}
