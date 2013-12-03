package com.duali.nfc.manager.ui.dialog.composites;

import info.ineighborhood.cardme.vcard.VCard;
import info.ineighborhood.cardme.vcard.features.AddressFeature;
import info.ineighborhood.cardme.vcard.features.EmailFeature;
import info.ineighborhood.cardme.vcard.features.FormattedNameFeature;
import info.ineighborhood.cardme.vcard.features.NameFeature;
import info.ineighborhood.cardme.vcard.features.OrganizationFeature;
import info.ineighborhood.cardme.vcard.features.PhotoFeature;
import info.ineighborhood.cardme.vcard.features.TelephoneFeature;
import info.ineighborhood.cardme.vcard.types.parameters.TelephoneParameterType;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.manager.ui.utils.ImageUtil;
import com.duali.nfc.ndef.records.Record;
import com.duali.nfc.ndef.records.VCardRecord;

public class CommercialVCardRecordComposite extends RecordComposite {
	private Text txtFirstName;
	private Text txtLastName;
	private Text txtOrganization;
	private Text txtAddress;
	private Text txtAdditionalAddress;
	private Text txtCity;
	private Text txtPostalCode;
	private Text txtCountry;
	private Text txtEmail;
	private Text txtPhone;
	private Text txtCell;
	private Text txtFax;
	private Canvas cvsPhoto;
	private static final int HORIZ_COMP_GAP = 5; 
	
	private Image imgPhoto;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CommercialVCardRecordComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		Group grpSmartposterRecord = new Group(this, SWT.NONE);
		grpSmartposterRecord.setBackgroundMode(SWT.INHERIT_DEFAULT);
		grpSmartposterRecord.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSmartposterRecord.setText("vCard Record");
		grpSmartposterRecord.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		grpSmartposterRecord.setLayout(new GridLayout(2, false));		
		
		Label lblPhoto = new Label(grpSmartposterRecord, SWT.NONE);
		lblPhoto.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPhoto.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblPhoto.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblPhoto.setText("Photo");
		
		final int canvasWidth = 90;
		final int canvasHeight = 100;
		cvsPhoto = new Canvas(grpSmartposterRecord, SWT.NONE);
		GridData gd_cvsPhoto = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_cvsPhoto.widthHint = 90;
		gd_cvsPhoto.heightHint = 100;
		cvsPhoto.setLayoutData(gd_cvsPhoto);
		
		cvsPhoto.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				
				if ( imgPhoto == null  ) {
					Image imgTemp = new Image(getDisplay(), canvasWidth, canvasHeight);
					GC gc = new GC(imgTemp);

					gc.drawRectangle(new Rectangle(0, 0, canvasWidth-1, canvasHeight-1));
					gc.drawLine(0,0,canvasWidth,canvasHeight);
					gc.drawLine(canvasWidth,0,0,canvasHeight);
					gc.dispose();

					e.gc.drawImage(imgTemp, 0, 0);
					imgTemp.dispose();

					return;
				} else {
					Image copy = new Image(getDisplay(), imgPhoto, SWT.IMAGE_COPY);
					Image scaled = ImageUtil.resize(copy, canvasWidth, canvasHeight);

					GC gc = new GC(scaled);
					gc.drawRectangle(new Rectangle(0, 0, scaled.getBounds().width-1, scaled.getBounds().height-1));
					gc.dispose();

					e.gc.drawImage(scaled, 0, 0);									
					scaled.dispose();
				}				
			}
		});
		new Label(grpSmartposterRecord, SWT.NONE);
		
		new Label(grpSmartposterRecord, SWT.NONE);
		
		Label lblNewLabel = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel.setText("First Name");

		txtFirstName = new Text(grpSmartposterRecord, SWT.BORDER);
		txtFirstName.setEditable(false);
		txtFirstName.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtFirstName.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtFirstName.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtFirstName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_1 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_1.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_1.setText("Last Name");

		txtLastName = new Text(grpSmartposterRecord, SWT.BORDER);
		txtLastName.setEditable(false);
		txtLastName.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtLastName.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtLastName.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtLastName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_2 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_2.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_2.setText("Organization");

		txtOrganization = new Text(grpSmartposterRecord, SWT.BORDER);
		txtOrganization.setEditable(false);
		txtOrganization.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtOrganization.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtOrganization.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtOrganization.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_3 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_3.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_3.setAlignment(SWT.RIGHT);
		lblNewLabel_3.setText("Address");

		txtAddress = new Text(grpSmartposterRecord, SWT.BORDER);
		txtAddress.setEditable(false);
		txtAddress.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtAddress.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtAddress.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_4 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_4.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_4.setAlignment(SWT.RIGHT);
		lblNewLabel_4.setText("Additional address");

		txtAdditionalAddress = new Text(grpSmartposterRecord, SWT.BORDER);
		txtAdditionalAddress.setEditable(false);
		txtAdditionalAddress.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtAdditionalAddress.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtAdditionalAddress.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtAdditionalAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_5 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_5.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_5.setAlignment(SWT.RIGHT);
		lblNewLabel_5.setText("City");

		txtCity = new Text(grpSmartposterRecord, SWT.BORDER);
		txtCity.setEditable(false);
		txtCity.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtCity.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtCity.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtCity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_6 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_6.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_6.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_6.setAlignment(SWT.RIGHT);
		lblNewLabel_6.setText("Postal code");

		txtPostalCode = new Text(grpSmartposterRecord, SWT.BORDER);
		txtPostalCode.setEditable(false);
		txtPostalCode.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtPostalCode.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtPostalCode.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtPostalCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_7 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_7.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_7.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_7.setAlignment(SWT.RIGHT);
		lblNewLabel_7.setText("Country");

		txtCountry = new Text(grpSmartposterRecord, SWT.BORDER);
		txtCountry.setEditable(false);
		txtCountry.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtCountry.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtCountry.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtCountry.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_8 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_8.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_8.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_8.setAlignment(SWT.RIGHT);
		lblNewLabel_8.setText("E-mail");

		txtEmail = new Text(grpSmartposterRecord, SWT.BORDER);
		txtEmail.setEditable(false);
		txtEmail.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtEmail.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtEmail.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_9 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_9.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_9.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_9.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_9.setAlignment(SWT.RIGHT);
		lblNewLabel_9.setText("Phone");

		txtPhone = new Text(grpSmartposterRecord, SWT.BORDER);
		txtPhone.setEditable(false);
		txtPhone.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtPhone.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtPhone.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtPhone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_10 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_10.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_10.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_10.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_10.setAlignment(SWT.RIGHT);
		lblNewLabel_10.setText("Cell");

		txtCell = new Text(grpSmartposterRecord, SWT.BORDER);
		txtCell.setEditable(false);
		txtCell.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtCell.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtCell.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtCell.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_11 = new Label(grpSmartposterRecord, SWT.NONE);
		lblNewLabel_11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_11.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblNewLabel_11.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblNewLabel_11.setAlignment(SWT.RIGHT);
		lblNewLabel_11.setText("Fax");

		txtFax = new Text(grpSmartposterRecord, SWT.BORDER);
		txtFax.setEditable(false);
		txtFax.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtFax.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtFax.setBackground(DisplayUtils.CURRENT_DISPLAY.getSystemColor(SWT.COLOR_WHITE));
		txtFax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	@Override
	public void initData(Record record) {
		if ( record == null) {
			imgPhoto = null;
			cvsPhoto.redraw();
			
			txtFirstName.setText("");
			txtLastName.setText("");
			txtOrganization.setText("");
			txtAddress.setText("");
			txtAdditionalAddress.setText("");
			txtCity.setText("");
			txtPostalCode.setText("");
			txtCountry.setText("");
			txtEmail.setText("");
			txtPhone.setText("");
			txtCell.setText("");
			txtFax.setText("");	
		} else {
			VCardRecord vCardRecord = (VCardRecord) record;
			VCard vCard = vCardRecord.getvCard();

			Iterator<PhotoFeature> photos = vCard.getPhotos();
			if (photos.hasNext()) {
				PhotoFeature photoFeature = photos.next();
				if(photoFeature != null) {
					byte[] photo = photoFeature.getPhoto();
					imgPhoto = ImageUtil.convertByteArraToImage(getDisplay(), photo);				
				} else {
					imgPhoto = null;
										
				}
			}	
			cvsPhoto.redraw();
			
			NameFeature nameFeature = vCard.getName();
			
			FormattedNameFeature formattedNameFeature = vCard.getFormattedName();

			if (formattedNameFeature != null) {
				String formattedName = formattedNameFeature.getFormattedName();
				if (formattedName != null)
					txtFirstName.setText(formattedName);
				else
					txtFirstName.setText("");
				
					txtLastName.setText("");
			}
			
			if (nameFeature != null) {
				String lastName = nameFeature.getFamilyName();
				String firstName = nameFeature.getGivenName();
				if (lastName != null)
					txtLastName.setText(lastName);
				else
					txtLastName.setText("");

				if (lastName != null)
					txtFirstName.setText(firstName);
				else
					txtFirstName.setText("");
			}

			//			FormattedNameFeature name = vCard.getFormattedName();
			//			if (name != null)
			//				txtFirstName.setText(name.getFormattedName());

			OrganizationFeature organizationFeature = vCard.getOrganizations();

			if (organizationFeature != null) {
				Iterator<String>  organizations = organizationFeature.getOrganizations();


				while(organizations.hasNext()) {
					String organization = organizations.next();
					if(organization != null && organization.trim().length() > 0) {
						txtOrganization.setText(organization);
					} else {
						txtOrganization.setText("");
					}
				}				
			}

			Iterator<AddressFeature> addressFeature = vCard.getAddresses();
			if (addressFeature != null) {
				while(addressFeature.hasNext()) {
					AddressFeature address = addressFeature.next();
					txtCountry.setText(address.getCountryName() == null ? "" : address.getCountryName());
					txtAddress.setText(address.getStreetAddress() == null ? "" : address.getStreetAddress());
					txtAdditionalAddress.setText(address.getExtendedAddress() == null ? "" : address.getExtendedAddress());
					txtCity.setText(address.getLocality() == null ? "" : address.getLocality());
					txtPostalCode.setText(address.getPostalCode() == null ? "" : address.getPostalCode());
				}
			}

			Iterator<TelephoneFeature> tel = vCard.getTelephoneNumbers();
			
			boolean work = false;
			boolean cell = false;
			boolean fax = false;
			if (tel != null) {
				while(tel.hasNext()) {
					TelephoneFeature telepone = tel.next();				
					List<TelephoneParameterType> telephoneParameterTypesList= telepone.getTelephoneParameterTypesList();
					for (TelephoneParameterType type : telephoneParameterTypesList) {
						if (type.getType().toUpperCase().equals("WORK") ) {
							work = true;
						} else if (type.getType().toUpperCase().equals("FAX")) {
							fax = true;	
						} else if (type.getType().toUpperCase().equals("CELL")) {
							cell = true;
						}
						/*if (type.getType().toUpperCase().equals("WORK") ) {
							txtPhone.setText(telepone.getTelephone() == null ? "" : telepone.getTelephone());
							break;
						} else if (type.getType().toUpperCase().equals("FAX")) {
							txtFax.setText(telepone.getTelephone() == null ? "" : telepone.getTelephone());	
						} else if (type.getType().toUpperCase().equals("CELL")) {
							txtCell.setText(telepone.getTelephone() == null ? "" : telepone.getTelephone());	
						}*/
					}
					
					if (work && fax) {
						txtFax.setText(telepone.getTelephone() == null ? "" : telepone.getTelephone());
					} else if (work && cell) {
						txtCell.setText(telepone.getTelephone() == null ? "" : telepone.getTelephone());
					} else if (work && !fax && !cell) {
						txtPhone.setText(telepone.getTelephone() == null ? "" : telepone.getTelephone());
					}
				}
			}

			Iterator<EmailFeature> emails = vCard.getEmails();
			if (emails != null) {
				while(emails.hasNext()) {
					EmailFeature email = emails.next();
					if(email != null && email.getEmail() != null && email.getEmail().trim().length() > 0)
						txtEmail.setText(email.getEmail() == null ? "" : email.getEmail());	
				}				
			}
		}
	}

	@Override
	public void updateData(Record record) {
		// TODO Auto-generated method stub

	}
}