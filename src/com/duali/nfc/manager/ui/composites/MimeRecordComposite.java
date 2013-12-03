package com.duali.nfc.manager.ui.composites;

import java.util.Enumeration;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.duali.nfc.manager.ui.composites.mime.ApplicationXChOneTouchComposite;
import com.duali.nfc.manager.ui.composites.mime.FileSelectionComposite;
import com.duali.nfc.manager.ui.composites.mime.ImageJpegComposite;
import com.duali.nfc.manager.ui.res.MimeTypeResource;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.manager.ui.utils.OrderedProperties;

public class MimeRecordComposite extends AbstractRecordComposite {	
	private ApplicationXChOneTouchComposite applicationXChOneTouchComposite;
	private ImageJpegComposite imageJpegComposite;
	private FileSelectionComposite fileSelectionComposite;
	private Combo combo;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MimeRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("TYPE: ");
		lblNewLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		
		ComboViewer comboViewer = new ComboViewer(this, SWT.READ_ONLY);
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		combo.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if ( combo.getSelectionIndex() < 0)
					return;

				String selectedType = combo.getItem(combo.getSelectionIndex());
//				LOGGER.debug("PC/SC Device: " + selectedPcsc);

				if(selectedType == null)
					return;

				populateUI(selectedType);
				
			}
		});
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(null);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		
		OrderedProperties mimeTypeProperties = MimeTypeResource.getMimeTypeList();

		Enumeration<Object> enumerator = mimeTypeProperties.propertyNames();
		while (enumerator.hasMoreElements()) {
			Object object = (Object) enumerator.nextElement();
			//System.out.println("Key " + object);
			//System.out.println("Value " + uRProperties.get(object));
			
			comboViewer.add((String)mimeTypeProperties.get(object));
			comboViewer.setData((String)mimeTypeProperties.get(object), (String)object);
		}
		
		createApplicationXChOneTouchComposite(composite);
		createImageJpegComposite(composite);
		createFileSelectionComposite(composite);
		combo.select(0);		
		
		
	}
	
	private void createImageJpegComposite(Composite composite) {
		imageJpegComposite = new ImageJpegComposite(composite, SWT.NONE);
		imageJpegComposite.setBounds(0, 0, 400, 250);
		
	}

	private void createApplicationXChOneTouchComposite(Composite composite) {
		applicationXChOneTouchComposite = new ApplicationXChOneTouchComposite(composite, SWT.NONE);
		applicationXChOneTouchComposite.setBounds(0, 0, 400, 250);
	}
	
	private void createFileSelectionComposite(Composite composite) {
		fileSelectionComposite = new FileSelectionComposite(composite, SWT.NONE);
		fileSelectionComposite.setBounds(0, 0, 400, 250);
		
		if (combo.getSelectionIndex() != -1)
			fileSelectionComposite.setType(combo.getItem(combo.getSelectionIndex()));
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void addRecord(TableViewer tbvRecord) {
		// 활성호된 컴포지트 찾기
		
		if (applicationXChOneTouchComposite.isVisible()) {
			applicationXChOneTouchComposite.addRecord(tbvRecord);
		} 
		
		if (imageJpegComposite.isVisible()) {
			imageJpegComposite.addRecord(tbvRecord);
		} 
		
		if (fileSelectionComposite.isVisible()) {
			fileSelectionComposite.addRecord(tbvRecord);
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
	
	private void populateUI(String type) {
				
		if(ApplicationXChOneTouchComposite.TYPE.equals(type)) {

			applicationXChOneTouchComposite.clear();
			applicationXChOneTouchComposite.setDefaultData();
			
			applicationXChOneTouchComposite.setVisible(true);
			imageJpegComposite.setVisible(false);
			fileSelectionComposite.setVisible(false);
		} else if(ImageJpegComposite.TYPE.equals(type)) {

			imageJpegComposite.clear();
			imageJpegComposite.setDefaultData();
			
			imageJpegComposite.setVisible(true);
			applicationXChOneTouchComposite.setVisible(false);
			fileSelectionComposite.setVisible(false);
		} else {
			fileSelectionComposite.clear();
			fileSelectionComposite.setDefaultData();
			
			fileSelectionComposite.setVisible(true);
			applicationXChOneTouchComposite.setVisible(false);
			imageJpegComposite.setVisible(false);
		}
	}
}
