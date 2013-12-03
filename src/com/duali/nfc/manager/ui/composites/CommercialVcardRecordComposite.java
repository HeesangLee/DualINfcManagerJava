package com.duali.nfc.manager.ui.composites;

import info.ineighborhood.cardme.io.CompatibilityMode;
import info.ineighborhood.cardme.io.FoldingScheme;
import info.ineighborhood.cardme.io.VCardWriter;
import info.ineighborhood.cardme.vcard.EncodingType;
import info.ineighborhood.cardme.vcard.VCard;
import info.ineighborhood.cardme.vcard.VCardImpl;
import info.ineighborhood.cardme.vcard.VCardVersion;
import info.ineighborhood.cardme.vcard.features.AddressFeature;
import info.ineighborhood.cardme.vcard.features.EmailFeature;
import info.ineighborhood.cardme.vcard.features.FormattedNameFeature;
import info.ineighborhood.cardme.vcard.features.NameFeature;
import info.ineighborhood.cardme.vcard.features.OrganizationFeature;
import info.ineighborhood.cardme.vcard.features.PhotoFeature;
import info.ineighborhood.cardme.vcard.features.TelephoneFeature;
import info.ineighborhood.cardme.vcard.types.AddressType;
import info.ineighborhood.cardme.vcard.types.BeginType;
import info.ineighborhood.cardme.vcard.types.EmailType;
import info.ineighborhood.cardme.vcard.types.EndType;
import info.ineighborhood.cardme.vcard.types.FormattedNameType;
import info.ineighborhood.cardme.vcard.types.NameType;
import info.ineighborhood.cardme.vcard.types.OrganizationType;
import info.ineighborhood.cardme.vcard.types.PhotoType;
import info.ineighborhood.cardme.vcard.types.TelephoneType;
import info.ineighborhood.cardme.vcard.types.VersionType;
import info.ineighborhood.cardme.vcard.types.media.ImageMediaType;
import info.ineighborhood.cardme.vcard.types.parameters.AddressParameterType;
import info.ineighborhood.cardme.vcard.types.parameters.EmailParameterType;
import info.ineighborhood.cardme.vcard.types.parameters.ParameterTypeStyle;
import info.ineighborhood.cardme.vcard.types.parameters.TelephoneParameterType;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.duali.nfc.manager.ui.utils.AppImages;
import com.duali.nfc.manager.ui.utils.AppLocale;
import com.duali.nfc.manager.ui.utils.DisplayUtils;
import com.duali.nfc.manager.ui.utils.FontUtils;
import com.duali.nfc.manager.ui.utils.ImageUtil;
import com.duali.nfc.ndef.records.VCardRecord;
import com.duali.utils.FileUtil;

public class CommercialVcardRecordComposite extends AbstractRecordComposite {

	private Text txtFirstName;
	private Text txtLastName;
	private Text txtOrganization;
	private Text txtAddress;
	private Text txtAdditionalAddress;
	private Text txtEmail;
	private Text txtCity;
	private Text txtPostalCode;
	private Text txtCountry;
	private Text txtPhone;
	private Text txtCell;
	private Text txtFax;

	private static final int HORIZ_COMP_GAP = 5; 
	//	private FormData textFormData_1;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CommercialVcardRecordComposite(Composite parent, int style) {
		super(parent, style);
		final FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		this.setLayout(layout);

		createPhotoElements();
		createFirstNameElements();
		createLastNameElements();
		createOrganizationElements();
		createAddressElements();
		createAdditionalAddressElements();
		createCityElements();
		createPostalCodeElements();
		createCountryElements();
		createEmailElements();
		createPhoneNumElements();
		createCellNumElements();
		createFaxElements();

		setDefaultData();
	}

	private Canvas cvsPhoto;
	//	private Button btnBrowse;
	private Text txtImagePath;
	//	private FormData fd_btnBrowse;
	private Label lblPhoto;
	private Button btnDelete;
	private Button btnBrowse;
	private Text txtPhotoSize;
	private void createPhotoElements() {
		final FormData fd_lblPhoto = new FormData();
		fd_lblPhoto.height = 15;
		fd_lblPhoto.right  = new FormAttachment(25, 100, 1);
		fd_lblPhoto.top = new FormAttachment(3);

		lblPhoto = new Label(this, SWT.NONE);
		lblPhoto.setText( AppLocale.getText("BSC.PHOTO") + ":");
		lblPhoto.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lblPhoto.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lblPhoto.setLayoutData(fd_lblPhoto); 

		final FormData txtImagePathFormData = new FormData();
		txtImagePathFormData.width = 90;
		txtImagePathFormData.right  = new FormAttachment(25, 100, 1);
		txtImagePathFormData.top = new FormAttachment(3);		

		FormData fd_txtImagePath = new FormData();		
		fd_txtImagePath.height = 15;
		fd_txtImagePath.right  = new FormAttachment(90, 100, 0);
		fd_txtImagePath.left = new FormAttachment(lblPhoto, 10);
		fd_txtImagePath.top = new FormAttachment(3);

		txtImagePath = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		txtImagePath.setLayoutData(fd_txtImagePath);
		txtImagePath.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtImagePath.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);

		final int canvasWidth = 90;
		final int canvasHeight = 100;

		final FormData cvsPhotoFormData = new FormData();
		cvsPhotoFormData.width = canvasWidth;
		cvsPhotoFormData.height = canvasHeight;
		cvsPhotoFormData.right  = new FormAttachment(25, 50, 0);
		cvsPhotoFormData.top = new FormAttachment(txtImagePath, HORIZ_COMP_GAP + 1);

		cvsPhoto = new Canvas(this, SWT.NONE);
		cvsPhoto.setLayoutData(cvsPhotoFormData);
		cvsPhoto.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				String imagePath = txtImagePath.getText();
				if ( imagePath.trim().length() <= 0  ) {
					Image imgEmptyPhoto = AppImages.IMAGE_EMPTY_PHOTO;
//					Image image = new Image(getDisplay(), canvasWidth, canvasHeight);
//					GC gc = new GC(image);
//
//					gc.drawRectangle(new Rectangle(0, 0, canvasWidth-1, canvasHeight-1));
//					gc.drawLine(0,0,canvasWidth,canvasHeight);
//					gc.drawLine(canvasWidth,0,0,canvasHeight);
//					gc.dispose();

					e.gc.drawImage(imgEmptyPhoto, 0, 0);
//					image.dispose();

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

		FormData fd_btnDelete = new FormData();
		fd_btnDelete.top = new FormAttachment(txtImagePath, 6);

		btnDelete = new Button(this, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtImagePath.setText("");
				cvsPhoto.redraw();
			}
		});
		btnDelete.setLayoutData(fd_btnDelete);
		btnDelete.setText("Delete");
		btnDelete.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		btnDelete.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);	

		final FormData fd_btnBrowse = new FormData();
		fd_btnBrowse.top = new FormAttachment(txtImagePath, 6);
		fd_btnBrowse.left = new FormAttachment(cvsPhoto, 6);

		btnBrowse = new Button(this, SWT.NONE);
		fd_btnDelete.left = new FormAttachment(btnBrowse, 6);
		btnBrowse.setText("Browse");
		btnBrowse.setLayoutData(fd_btnBrowse);
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

					cvsPhoto.redraw();
				}
			}
		});
	}

	private void createFirstNameElements() {
		final FormData firstNameLableData = new FormData();
		firstNameLableData.top = new FormAttachment(lblPhoto, 118);
		firstNameLableData.height = 15;
		firstNameLableData.right  = new FormAttachment(25, 100, 1);

		Label firstNameLabel = new Label(this, SWT.NONE);
		//		textFormData_1.left = new FormAttachment(firstNameLabel, 10);
		firstNameLabel.setText( AppLocale.getText("BSC.FIRST.NAME") + ":");
		firstNameLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		firstNameLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		firstNameLabel.setLayoutData(firstNameLableData); 

		final FormData textFormData = new FormData();
		textFormData.top = new FormAttachment(cvsPhoto, 6);
		textFormData.height = 15;
		textFormData.right  = new FormAttachment(90, 100, 0);
		textFormData.left = new FormAttachment(firstNameLabel, 10);

		txtFirstName = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtFirstName.setTextLimit(20);
		txtFirstName.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtFirstName.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtFirstName.setLayoutData(textFormData);
	}

	private void createLastNameElements() {
		final FormData lastNameLabelData = new FormData();
		lastNameLabelData.height = 15;
		lastNameLabelData.right  = new FormAttachment(25, 100, 1);
		lastNameLabelData.top = new FormAttachment(txtFirstName, HORIZ_COMP_GAP + 1);

		Label lastNameLabel = new Label(this, SWT.NONE);
		lastNameLabel.setText(AppLocale.getText("BSC.LAST.NAME") + ":");
		lastNameLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		lastNameLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		lastNameLabel.setLayoutData(lastNameLabelData);

		txtLastName = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtLastName.setTextLimit(20);
		final FormData textLastNameData = new FormData();
		textLastNameData.height = 15;
		textLastNameData.right  = new FormAttachment(90, 100, 0);
		textLastNameData.left = new FormAttachment(lastNameLabel,10);
		textLastNameData.top = new FormAttachment(txtFirstName, HORIZ_COMP_GAP);
		txtLastName.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtLastName.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtLastName.setLayoutData(textLastNameData);
		//textLastName.setText("Technologies");
	}

	private void createOrganizationElements() {
		final FormData organizationLabelData = new FormData();
		organizationLabelData.height = 15;
		organizationLabelData.right  = new FormAttachment(25, 100, 1);
		organizationLabelData.top = new FormAttachment(txtLastName, HORIZ_COMP_GAP + 1);

		Label organizationLabel = new Label(this, SWT.NONE);
		organizationLabel.setText(AppLocale.getText("ORGANZTN") + ":");
		organizationLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		organizationLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		organizationLabel.setLayoutData(organizationLabelData);

		txtOrganization = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtOrganization.setTextLimit(30);
		final FormData textOrganizationData = new FormData();
		textOrganizationData.height = 15;
		textOrganizationData.right  = new FormAttachment(90, 100, 0);
		textOrganizationData.left = new FormAttachment(organizationLabel, 10);
		textOrganizationData.top = new FormAttachment(txtLastName, HORIZ_COMP_GAP);
		txtOrganization.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtOrganization.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtOrganization.setLayoutData(textOrganizationData);
	}

	private void createAddressElements() {
		final FormData addressLabelData = new FormData();
		addressLabelData.height = 15;
		addressLabelData.right  = new FormAttachment(25, 100, 1);
		addressLabelData.top = new FormAttachment(txtOrganization, HORIZ_COMP_GAP + 1);

		Label addressLabel = new Label(this, SWT.NONE);
		addressLabel.setText(AppLocale.getText("ADDRS") + ":");
		addressLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		addressLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		addressLabel.setLayoutData(addressLabelData);


		txtAddress = new Text(this, SWT.SINGLE | SWT.BORDER); //new Text(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		txtAddress.setTextLimit(30);
		final FormData textAddressData = new FormData();
		textAddressData.height = 15;
		textAddressData.right  = new FormAttachment(90, 100, 0);
		textAddressData.left = new FormAttachment(addressLabel, 10);
		textAddressData.top = new FormAttachment(txtOrganization, HORIZ_COMP_GAP);
		txtAddress.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtAddress.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtAddress.setLayoutData(textAddressData);
		//textAddress.setText("C5 Thejaswini, Technopark Campus");
	}

	private void createAdditionalAddressElements() {
		final FormData addAddressLabelData = new FormData();
		addAddressLabelData.height = 15;
		addAddressLabelData.right  = new FormAttachment(25, 100, 1);
		addAddressLabelData.top = new FormAttachment(txtAddress, HORIZ_COMP_GAP + 1);

		Label additionalAddressLabel = new Label(this, SWT.NONE);
		additionalAddressLabel.setText(AppLocale.getText("BSC.ADD.ADRS"));
		additionalAddressLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		additionalAddressLabel.setForeground(DisplayUtils.COLOR_TAGMAN_BG_LIGHT_GRAY);
		additionalAddressLabel.setLayoutData(addAddressLabelData);

		txtAdditionalAddress = new Text(this, SWT.SINGLE | SWT.BORDER); //new Text(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		txtAdditionalAddress.setTextLimit(30);
		final FormData textAddAddressData = new FormData();
		textAddAddressData.height = 15;
		textAddAddressData.right  = new FormAttachment(90, 100, 0);
		textAddAddressData.left = new FormAttachment(additionalAddressLabel, 10);
		textAddAddressData.top = new FormAttachment(txtAddress, HORIZ_COMP_GAP);
		txtAdditionalAddress.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtAdditionalAddress.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtAdditionalAddress.setLayoutData(textAddAddressData);
		//textAdditionalAddress.setText("Trivandrum, Kazhakoottam");
	}

	private void createCityElements() {
		final FormData cityLabelData = new FormData();
		cityLabelData.height = 15;
		cityLabelData.right = new FormAttachment(25, 100, 1);
		cityLabelData.top = new FormAttachment(txtAdditionalAddress, HORIZ_COMP_GAP + 1);

		Label cityLabel = new Label(this, SWT.NONE);
		cityLabel.setText(AppLocale.getText("CITY") + ":");
		cityLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		cityLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		cityLabel.setLayoutData(cityLabelData);

		txtCity = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtCity.setTextLimit(20);
		final FormData textCityData = new FormData();
		textCityData.height = 15;
		textCityData.right  = new FormAttachment(90, 100, 0);
		textCityData.left = new FormAttachment(cityLabel, 10);
		textCityData.top = new FormAttachment(txtAdditionalAddress, HORIZ_COMP_GAP);
		txtCity.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtCity.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtCity.setLayoutData(textCityData);
		//textCity.setText("Trivandrum");
	}

	private void createPostalCodeElements() {
		final FormData postalCodeData = new FormData();
		postalCodeData.height = 15;
		postalCodeData.right  = new FormAttachment(25, 100, 1);
		postalCodeData.top = new FormAttachment(txtCity, HORIZ_COMP_GAP + 1);

		Label postalCodeLabel = new Label(this, SWT.NONE);
		postalCodeLabel.setText(AppLocale.getText("BSC.POSTAL.CODE") + ":");
		postalCodeLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		postalCodeLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		postalCodeLabel.setLayoutData(postalCodeData);

		txtPostalCode = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtPostalCode.setTextLimit(15);
		final FormData textPostalCodeData = new FormData();
		textPostalCodeData.height = 15;
		textPostalCodeData.right  = new FormAttachment(90, 100, 0);
		textPostalCodeData.left = new FormAttachment(postalCodeLabel, 10);
		textPostalCodeData.top = new FormAttachment(txtCity, HORIZ_COMP_GAP);
		txtPostalCode.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtPostalCode.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtPostalCode.setLayoutData(textPostalCodeData);
		txtPostalCode.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		//textPostalCode.setText("689503");
	}

	private void createCountryElements() {
		final FormData countryLabelData = new FormData();
		countryLabelData.height = 15;
		countryLabelData.right  = new FormAttachment(25, 100, 1);
		countryLabelData.top = new FormAttachment(txtPostalCode, HORIZ_COMP_GAP + 1);

		Label countryLabel = new Label(this, SWT.NONE);
		countryLabel.setText(AppLocale.getText("BSC.COUNTRY") + ":");
		countryLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		countryLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		countryLabel.setLayoutData(countryLabelData);

		txtCountry = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtCountry.setTextLimit(20);
		final FormData textCountryData = new FormData();
		textCountryData.height = 15;
		textCountryData.right  = new FormAttachment(90, 100, 0);
		textCountryData.left = new FormAttachment(countryLabel, 10);
		textCountryData.top = new FormAttachment(txtPostalCode, HORIZ_COMP_GAP);
		txtCountry.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtCountry.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtCountry.setLayoutData(textCountryData);
		//textCountry.setText("India");
	}

	private void createEmailElements() {
		final FormData emailLabelData = new FormData();
		emailLabelData.height = 15;
		emailLabelData.right  = new FormAttachment(25, 100, 1);
		emailLabelData.top = new FormAttachment(txtCountry, HORIZ_COMP_GAP + 1);

		Label emailLabel = new Label(this, SWT.NONE);
		emailLabel.setText( AppLocale.getText("EMAIL") + ":");
		emailLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		emailLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		emailLabel.setLayoutData(emailLabelData);

		txtEmail = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtEmail.setTextLimit(40);
		final FormData textEmailData = new FormData();
		textEmailData.height = 15;
		textEmailData.right  = new FormAttachment(90, 100, 0);
		textEmailData.left = new FormAttachment(emailLabel, 10);
		textEmailData.top = new FormAttachment(txtCountry, HORIZ_COMP_GAP);
		txtEmail.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtEmail.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtEmail.setLayoutData(textEmailData);
	}

	private void createPhoneNumElements() {
		final FormData phoneLabelData = new FormData();
		phoneLabelData.height = 15;
		phoneLabelData.right  = new FormAttachment(25, 100, 1);
		phoneLabelData.top = new FormAttachment(txtEmail, HORIZ_COMP_GAP + 1);

		Label phoneLabel = new Label(this, SWT.NONE);
		phoneLabel.setText(AppLocale.getText("PHONE") + ":");
		phoneLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		phoneLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		phoneLabel.setLayoutData(phoneLabelData);

		txtPhone = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtPhone.setTextLimit(20);
		final FormData textPhoneData = new FormData();
		textPhoneData.height = 15;
		textPhoneData.right  = new FormAttachment(90, 100, 0);
		textPhoneData.left = new FormAttachment(phoneLabel, 10);
		textPhoneData.top = new FormAttachment(txtEmail, HORIZ_COMP_GAP);
		txtPhone.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtPhone.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtPhone.setLayoutData(textPhoneData);
		//textPhone.setText("00919895313034");
		txtPhone.addListener(SWT.Verify, new Listener() {
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

	private void createCellNumElements() {
		final FormData fd_cellLabel = new FormData();
		fd_cellLabel.height = 15;
		fd_cellLabel.right  = new FormAttachment(25, 100, 1);
		fd_cellLabel.top = new FormAttachment(txtPhone, HORIZ_COMP_GAP + 1);

		Label cellLabel = new Label(this, SWT.NONE);
		cellLabel.setText(AppLocale.getText("CELL") + ":");
		cellLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		cellLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		cellLabel.setLayoutData(fd_cellLabel);

		txtCell = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtCell.setTextLimit(20);
		final FormData textPhoneData = new FormData();
		textPhoneData.height = 15;
		textPhoneData.right  = new FormAttachment(90, 100, 0);
		textPhoneData.left = new FormAttachment(cellLabel, 10);
		textPhoneData.top = new FormAttachment(txtPhone, HORIZ_COMP_GAP);
		txtCell.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtCell.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtCell.setLayoutData(textPhoneData);
		//textPhone.setText("00919895313034");
		txtCell.addListener(SWT.Verify, new Listener() {
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

	private void createFaxElements() {
		final FormData faxLabelData = new FormData();
		faxLabelData.height = 15;
		faxLabelData.right  = new FormAttachment(25, 100, 1);
		faxLabelData.top = new FormAttachment(txtCell, HORIZ_COMP_GAP + 1);

		Label faxLabel = new Label(this, SWT.NONE);
		faxLabel.setText(AppLocale.getText("BSC.FAX") + ":");
		faxLabel.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		faxLabel.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		faxLabel.setLayoutData(faxLabelData);

		txtFax = new Text(this, SWT.SINGLE | SWT.BORDER);
		txtFax.setTextLimit(20);
		final FormData textFaxData = new FormData();
		textFaxData.height = 15;
		textFaxData.right  = new FormAttachment(90, 100, 0);
		textFaxData.left = new FormAttachment(faxLabel, 10);
		textFaxData.top = new FormAttachment(txtCell, HORIZ_COMP_GAP);
		txtFax.setFont(FontUtils.FONT_HELVETICAS_BOLD_SIZE8);
		txtFax.setForeground(DisplayUtils.COLOR_TAGMAN_HEAD_DARK_GRAY);
		txtFax.setLayoutData(textFaxData);
		
		Label lblPhotoSize = new Label(this, SWT.NONE);
		FormData fd_lblPhotoSize = new FormData();
		fd_lblPhotoSize.top = new FormAttachment(btnBrowse, 10);
		fd_lblPhotoSize.left = new FormAttachment(cvsPhoto, 6);
		lblPhotoSize.setLayoutData(fd_lblPhotoSize);
		lblPhotoSize.setText("Size:");
		
		Label lblDescription = new Label(this, SWT.NONE);
		FormData fd_lblDescription = new FormData();
		fd_lblDescription.top = new FormAttachment(0, 100);
		fd_lblDescription.left = new FormAttachment(cvsPhoto, 6);
		lblDescription.setLayoutData(fd_lblDescription);
		lblDescription.setText("Supported formats: Jpeg or Jpg");
		
		txtPhotoSize = new Text(this, SWT.BORDER);
		txtPhotoSize.setEditable(false);
		FormData fd_txtPhotoSize = new FormData();
		fd_txtPhotoSize.top = new FormAttachment(lblPhotoSize, -3, SWT.TOP);
		fd_txtPhotoSize.right = new FormAttachment(lblPhotoSize, 115, SWT.RIGHT);
		fd_txtPhotoSize.left = new FormAttachment(lblPhotoSize, 20);
		txtPhotoSize.setLayoutData(fd_txtPhotoSize);

		//textFax.setText("00919895313034");
		txtFax.addListener(SWT.Verify, new Listener() {
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

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void clear() {
		txtImagePath.setText("");
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
	}

	public void setDefaultData() {
		txtImagePath.setText("");
		cvsPhoto.redraw();

		txtFirstName.setText("MINA");
		txtLastName.setText("JEON");
		txtOrganization.setText("DUALi");

		txtAddress.setText("Gyeonggi-do");
		txtAdditionalAddress.setText("Woncheon-dong, Yeongtong-gu");
		txtCity.setText("SuWon-si");
		txtCountry.setText("Korea");

		txtPostalCode.setText("443380");         
		txtEmail.setText("duali@duali.com");
		txtPhone.setText("+82-31-213-0074");
		txtCell.setText("+82-10-3381-7576");
		txtFax.setText("+82-31-213-0078");
	}

	public void addRecord(TableViewer tbvRecord) {		
		if(txtFirstName.getText() == null 
				|| txtFirstName.getText().trim().equals("") 
		){
			showInfoMessageBox(AppLocale.getText("MSG.ENTER.VALID", new String[]{AppLocale.getText("BSC.FIRST.NAME")}));
			return;
		}

		if(txtLastName.getText() == null 
				|| txtLastName.getText().trim().equals("") 
		){
			showInfoMessageBox(AppLocale.getText("MSG.ENTER.VALID", new String[]{AppLocale.getText("BSC.LAST.NAME")}));
			return;
		}

		if(txtPhone.getText() == null 
				|| txtPhone.getText().trim().equals("") ){
			showInfoMessageBox(AppLocale.getText("MSG.ENTER.VALID", new String[]{AppLocale.getText("PHNNUM")}));
			return;
		}
		if(txtPhone.getText().trim().length() < 7 ){
			showInfoMessageBox(AppLocale.getText("MSG.TEL.NUM.ATLST"));
			return;
		}



		VCard vcard = new VCardImpl();
		vcard.setVersion(new VersionType(VCardVersion.V3_0));
		BeginType begin = new BeginType();
		vcard.setBegin(begin);

		String photoPath = txtImagePath.getText();

		File file = new File(photoPath);
		try {
			if (file.exists()) {
				PhotoFeature photo1 = new PhotoType();
				photo1.setCompression(false);
				photo1.setEncodingType(EncodingType.BASE64);
				photo1.setImageMediaType(ImageMediaType.JPEG);
				byte[] tuxPicture1 = FileUtil.getBytesFromFile(file);
				photo1.setPhoto(tuxPicture1);
				vcard.addPhoto(photo1);
			}
		} catch (IOException e) {
//			e.printStackTrace();
		}	

		NameFeature name = new NameType();
		name.setFamilyName(txtLastName.getText());
		name.setGivenName(txtFirstName.getText());
		//		name.addHonorificPrefix("Mr.");
		//		name.addHonorificSuffix("I");
		//		name.addAdditionalName("Johny");
		vcard.setName(name);

		FormattedNameFeature formattedName = new FormattedNameType();
		formattedName.setFormattedName(txtLastName.getText() + " " + txtFirstName.getText() );
		vcard.setFormattedName(formattedName);

		String organization = txtOrganization.getText();

		if(organization != null && organization.trim().length() > 0) {
			OrganizationFeature organizations = new OrganizationType();
			organizations.addOrganization(organization);
			vcard.setOrganizations(organizations);
		}

		AddressFeature address1 = new AddressType();

		String additionalAddress = txtAdditionalAddress.getText();
		String country = txtCountry.getText();
		String city = txtCity.getText();
		String postalCode = txtPostalCode.getText();
		String address = txtAddress.getText();

		if(additionalAddress != null && additionalAddress.trim().length() > 0) {
			address1.setExtendedAddress(additionalAddress);	
		}

		if(country != null && country.trim().length() > 0) {
			address1.setCountryName(country);
		}

		if(city != null && city.trim().length() > 0) {
			address1.setLocality(city);
		}
		if(postalCode != null && postalCode.trim().length() > 0) {
			address1.setPostalCode(postalCode);
		}
		if(address != null && address.trim().length() > 0) {
			address1.setStreetAddress(address);
		}

		address1.addAddressParameterType(AddressParameterType.WORK);
		address1.addAddressParameterType(AddressParameterType.PARCEL);
		address1.addAddressParameterType(AddressParameterType.PREF);

		if(additionalAddress.trim().length() > 0 || 
				country != null && country.trim().length() > 0 ||
				city != null && city.trim().length() > 0 ||
				postalCode != null && postalCode.trim().length() > 0 ||
				address != null && address.trim().length() > 0) {
			vcard.addAddress(address1);
		}

		TelephoneFeature telephone = new TelephoneType();
		telephone.setTelephone(txtPhone.getText());		
		telephone.addTelephoneParameterType(TelephoneParameterType.WORK);
		telephone.setParameterTypeStyle(ParameterTypeStyle.PARAMETER_VALUE_LIST);
		vcard.addTelephoneNumber(telephone);

		String cell = txtCell.getText();
		if(cell != null && cell.trim().length() > 0) {
			TelephoneFeature telephone2 = new TelephoneType();
			telephone2.setTelephone(cell);
			telephone2.addTelephoneParameterType(TelephoneParameterType.CELL);			
			telephone2.setParameterTypeStyle(ParameterTypeStyle.PARAMETER_LIST);
			vcard.addTelephoneNumber(telephone2);
		}

		String fax = txtFax.getText();
		if(fax != null && fax.trim().length() > 0) {
			TelephoneFeature telephone2 = new TelephoneType();
			telephone2.setTelephone(fax);
			telephone2.addTelephoneParameterType(TelephoneParameterType.FAX);
			telephone2.addTelephoneParameterType(TelephoneParameterType.WORK);
			telephone2.setParameterTypeStyle(ParameterTypeStyle.PARAMETER_LIST);
			vcard.addTelephoneNumber(telephone2);
		}

		String email = txtEmail.getText();
		if(email != null && email.trim().length() > 0) {
			EmailFeature emailFeature = new EmailType();
			emailFeature.setEmail(txtEmail.getText());
			emailFeature.addEmailParameterType(EmailParameterType.WORK);
			emailFeature.addEmailParameterType(EmailParameterType.INTERNET);
			emailFeature.addEmailParameterType(EmailParameterType.PREF);
			vcard.addEmail(emailFeature);
		}

		// Create VCard Writer
		VCardWriter vcardWriter = new VCardWriter();
		vcardWriter.setOutputVersion(VCardVersion.V3_0);
		vcardWriter.setFoldingScheme(FoldingScheme.NONE);
		vcardWriter.setCompatibilityMode(CompatibilityMode.RFC2426);

		EndType end = new EndType();
		vcard.setEnd(end);

		VCardRecord vCardRecord = new VCardRecord(vcard);        
		addRecord(tbvRecord, vCardRecord);
	}

}
