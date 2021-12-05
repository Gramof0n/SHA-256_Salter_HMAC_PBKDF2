package gui;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import core.HMAC;
import core.PBKDF2;
import core.PasswordSalter;
import utils.Formating;

public class gui {

	private JFrame frmSaltHmacPbkdf;

	private List<JRadioButton> radioButtons = new ArrayList<JRadioButton>();
	private HMAC hmac = new HMAC();
	private PasswordSalter salter = new PasswordSalter();
	private PBKDF2 pbkdf = new PBKDF2();

	private JTextField textField;
	private JTextField txtHmacKey;
	private JTextField txtHmacText;
	private JTextField txtHmacTextKey;
	private JTextField txtSaltPass;
	private JTextField txtSaltSalt;
	private JTextField txtPbkdfPassword;
	private JTextField txtPbkdfSalt;

	private File selectedFile;
	private byte[] fileBytes;

	private CardLayout button_card_layout = new CardLayout(0, 0);
	private JTextArea txtaOutput = new JTextArea();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui window = new gui();
					window.frmSaltHmacPbkdf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSaltHmacPbkdf = new JFrame();
		frmSaltHmacPbkdf.setTitle("Salt HMAC PBKDF2");
		frmSaltHmacPbkdf.setResizable(false);
		frmSaltHmacPbkdf.setBounds(100, 100, 857, 526);
		frmSaltHmacPbkdf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSaltHmacPbkdf.getContentPane().setLayout(null);

		JPanel card_panel_buttons = new JPanel();

		JPanel side_menu = new JPanel();
		side_menu.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		side_menu.setBounds(10, 10, 206, 457);
		frmSaltHmacPbkdf.getContentPane().add(side_menu);
		side_menu.setLayout(new GridLayout(4, 2, 0, 0));

		JPanel panel_title = new JPanel();
		side_menu.add(panel_title);
		panel_title.setLayout(null);

		JLabel lblNewLabel = new JLabel("Select operation");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(26, 28, 148, 56);
		lblNewLabel.setFont(new Font("Yu Mincho Demibold", Font.PLAIN, 18));
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		panel_title.add(lblNewLabel);

		JPanel panel_salt_menu = new JPanel();
		panel_salt_menu.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		side_menu.add(panel_salt_menu);
		panel_salt_menu.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_1 = new JLabel("Salt password");
		panel_salt_menu.add(lblNewLabel_1);

		JRadioButton rdbthSalt = new JRadioButton("");
		panel_salt_menu.add(rdbthSalt);

		JPanel panel_hmac_menu = new JPanel();
		panel_hmac_menu.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		side_menu.add(panel_hmac_menu);
		panel_hmac_menu.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_1_1 = new JLabel("Generate HMAC");
		panel_hmac_menu.add(lblNewLabel_1_1);

		JRadioButton rdbtnHmac = new JRadioButton("");
		panel_hmac_menu.add(rdbtnHmac);

		JPanel panel_pbkdf2_menu = new JPanel();
		panel_pbkdf2_menu.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		side_menu.add(panel_pbkdf2_menu);
		panel_pbkdf2_menu.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_2 = new JLabel("PBKDF2");
		panel_pbkdf2_menu.add(lblNewLabel_2);

		JRadioButton rdbtnPbkdf2 = new JRadioButton("");
		panel_pbkdf2_menu.add(rdbtnPbkdf2);

		JPanel panel_output = new JPanel();
		panel_output.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_output.setBounds(226, 252, 607, 166);
		frmSaltHmacPbkdf.getContentPane().add(panel_output);
		panel_output.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 587, 146);
		panel_output.add(scrollPane);

		scrollPane.setViewportView(txtaOutput);
		txtaOutput.setLineWrap(true);
		txtaOutput.setEditable(false);

		JPanel card_panel = new JPanel();
		CardLayout cardLayout = new CardLayout(0, 0);
		card_panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		card_panel.setBounds(227, 10, 606, 232);
		frmSaltHmacPbkdf.getContentPane().add(card_panel);
		card_panel.setLayout(cardLayout);

		JPanel p_hmac = new JPanel();
		CardLayout hmac_cardLayout = new CardLayout(0, 0);
		card_panel.add(p_hmac, "p_hmac");
		p_hmac.setLayout(hmac_cardLayout);

		JPanel p_hash_file = new JPanel();
		p_hmac.add(p_hash_file, "p_hash_file");
		p_hash_file.setLayout(null);

		JLabel lblNewLabel_1_2 = new JLabel("Choose file:");
		lblNewLabel_1_2.setBounds(10, 10, 123, 13);
		p_hash_file.add(lblNewLabel_1_2);

		JButton btnUpload = new JButton("Browse");
		btnUpload.setBounds(105, 10, 485, 21);
		p_hash_file.add(btnUpload);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(105, 50, 485, 19);
		p_hash_file.add(textField);

		JLabel lblNewLabel_2_1 = new JLabel("File path: ");
		lblNewLabel_2_1.setBounds(10, 49, 99, 13);
		p_hash_file.add(lblNewLabel_2_1);

		JLabel lblNewLabel_3 = new JLabel("Key:");
		lblNewLabel_3.setBounds(10, 90, 64, 13);
		p_hash_file.add(lblNewLabel_3);

		txtHmacKey = new JTextField();
		txtHmacKey.setToolTipText("64 bit hex or 32 bit string");
		txtHmacKey.setColumns(10);
		txtHmacKey.setBounds(105, 87, 485, 19);
		p_hash_file.add(txtHmacKey);

		JLabel lblNewLabel_3_1 = new JLabel("Key operations: ");
		lblNewLabel_3_1.setBounds(10, 134, 142, 13);
		p_hash_file.add(lblNewLabel_3_1);

		JButton btnHmacGenerateKey = new JButton("Generate");
		btnHmacGenerateKey.setBounds(105, 130, 113, 21);
		p_hash_file.add(btnHmacGenerateKey);

		JButton btnHmacSaveToFile = new JButton("Save to file");
		btnHmacSaveToFile.setBounds(295, 130, 113, 21);
		p_hash_file.add(btnHmacSaveToFile);

		JButton btnHmacLoadFromFile = new JButton("Load from file");
		btnHmacLoadFromFile.setBounds(477, 130, 113, 21);
		p_hash_file.add(btnHmacLoadFromFile);

		JLabel lblNewLabel_4 = new JLabel("Generate HMAC");
		lblNewLabel_4.setBounds(10, 185, 123, 13);
		p_hash_file.add(lblNewLabel_4);

		JButton btnGenerateHmac = new JButton("Generate");
		btnGenerateHmac.setBounds(105, 181, 113, 21);
		p_hash_file.add(btnGenerateHmac);

		JLabel lblNewLabel_5 = new JLabel("or");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setBounds(251, 185, 45, 13);
		p_hash_file.add(lblNewLabel_5);

		JButton btnHashText = new JButton("Hmac from text");
		btnHashText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hmac_cardLayout.show(p_hmac, "p_hash_text");
				button_card_layout.show(card_panel_buttons, "btn_hmac_text");
			}
		});
		btnHashText.setBounds(315, 181, 123, 21);
		p_hash_file.add(btnHashText);

		JPanel p_hash_text = new JPanel();
		p_hmac.add(p_hash_text, "p_hash_text");
		p_hash_text.setLayout(null);

		JLabel lblNewLabel_6 = new JLabel("To hash:");
		lblNewLabel_6.setBounds(10, 33, 63, 13);
		p_hash_text.add(lblNewLabel_6);

		txtHmacText = new JTextField();
		txtHmacText.setBounds(105, 30, 485, 19);
		p_hash_text.add(txtHmacText);
		txtHmacText.setColumns(10);

		JLabel lblNewLabel_6_1 = new JLabel("Key:");
		lblNewLabel_6_1.setBounds(10, 73, 63, 13);
		p_hash_text.add(lblNewLabel_6_1);

		txtHmacTextKey = new JTextField();
		txtHmacTextKey.setColumns(10);
		txtHmacTextKey.setBounds(105, 70, 485, 19);
		p_hash_text.add(txtHmacTextKey);

		JLabel lblNewLabel_3_1_1 = new JLabel("Key ops: ");
		lblNewLabel_3_1_1.setBounds(10, 117, 71, 13);
		p_hash_text.add(lblNewLabel_3_1_1);

		JButton btnHmacTextGenerateKey = new JButton("Generate");
		btnHmacTextGenerateKey.setBounds(105, 113, 113, 21);
		p_hash_text.add(btnHmacTextGenerateKey);

		JButton btnHmacTextSaveToFile = new JButton("Save to file");
		btnHmacTextSaveToFile.setBounds(295, 113, 113, 21);
		p_hash_text.add(btnHmacTextSaveToFile);

		JButton btnHmacTextLoadFromFile = new JButton("Load from file");
		btnHmacTextLoadFromFile.setBounds(477, 113, 113, 21);
		p_hash_text.add(btnHmacTextLoadFromFile);

		JLabel lblNewLabel_7 = new JLabel("Generate hmac:");
		lblNewLabel_7.setBounds(10, 164, 96, 13);
		p_hash_text.add(lblNewLabel_7);

		JButton btnHmacTextGenerate = new JButton("Hash text");
		btnHmacTextGenerate.setBounds(105, 160, 113, 21);
		p_hash_text.add(btnHmacTextGenerate);

		JLabel lblNewLabel_8 = new JLabel("or");
		lblNewLabel_8.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_8.setBounds(228, 164, 45, 13);
		p_hash_text.add(lblNewLabel_8);

		JButton btnSwithToFile = new JButton("Hash file");
		btnSwithToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hmac_cardLayout.show(p_hmac, "p_hash_file");
				button_card_layout.show(card_panel_buttons, "btn_hmac");
			}
		});
		btnSwithToFile.setBounds(295, 160, 113, 21);
		p_hash_text.add(btnSwithToFile);

		JPanel p_salt = new JPanel();
		card_panel.add(p_salt, "p_salt");
		p_salt.setLayout(null);

		JLabel lblNewLabel_6_2 = new JLabel("Password:");
		lblNewLabel_6_2.setBounds(10, 25, 63, 13);
		p_salt.add(lblNewLabel_6_2);

		txtSaltPass = new JTextField();
		txtSaltPass.setColumns(10);
		txtSaltPass.setBounds(83, 22, 507, 19);
		p_salt.add(txtSaltPass);

		JLabel lblNewLabel_6_1_1 = new JLabel("Salt:");
		lblNewLabel_6_1_1.setBounds(10, 65, 63, 13);
		p_salt.add(lblNewLabel_6_1_1);

		txtSaltSalt = new JTextField();
		txtSaltSalt.setColumns(10);
		txtSaltSalt.setBounds(83, 62, 507, 19);
		p_salt.add(txtSaltSalt);

		JLabel lblNewLabel_3_1_1_1 = new JLabel("Salt ops:");
		lblNewLabel_3_1_1_1.setBounds(10, 109, 71, 13);
		p_salt.add(lblNewLabel_3_1_1_1);

		JButton btnSaltGenerate = new JButton("Generate");
		btnSaltGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSaltGenerate.setBounds(83, 105, 113, 21);
		p_salt.add(btnSaltGenerate);

		JButton btnSaltSaveToFile = new JButton("Save to file");
		btnSaltSaveToFile.setBounds(287, 105, 113, 21);
		p_salt.add(btnSaltSaveToFile);

		JButton btnSaltLoadFromFile = new JButton("Load from file");
		btnSaltLoadFromFile.setBounds(477, 105, 113, 21);
		p_salt.add(btnSaltLoadFromFile);

		JButton btnSaltPassword = new JButton("Salt password");
		btnSaltPassword.setBounds(83, 152, 123, 21);
		p_salt.add(btnSaltPassword);

		JLabel lblNewLabel_7_1 = new JLabel("Salt:");
		lblNewLabel_7_1.setBounds(10, 156, 45, 13);
		p_salt.add(lblNewLabel_7_1);

		JPanel p_pbkdf2 = new JPanel();
		card_panel.add(p_pbkdf2, "p_pbkdf2");
		p_pbkdf2.setLayout(null);

		txtPbkdfPassword = new JTextField();
		txtPbkdfPassword.setColumns(10);
		txtPbkdfPassword.setBounds(83, 22, 507, 19);
		p_pbkdf2.add(txtPbkdfPassword);

		JLabel lblNewLabel_6_2_1 = new JLabel("Password:");
		lblNewLabel_6_2_1.setBounds(10, 25, 63, 13);
		p_pbkdf2.add(lblNewLabel_6_2_1);

		JLabel lblNewLabel_6_1_1_1 = new JLabel("Salt:");
		lblNewLabel_6_1_1_1.setBounds(10, 65, 63, 13);
		p_pbkdf2.add(lblNewLabel_6_1_1_1);

		txtPbkdfSalt = new JTextField();
		txtPbkdfSalt.setColumns(10);
		txtPbkdfSalt.setBounds(83, 62, 507, 19);
		p_pbkdf2.add(txtPbkdfSalt);

		JButton btnPbkdfGenerateSalt = new JButton("Generate");
		btnPbkdfGenerateSalt.setBounds(83, 143, 113, 21);
		p_pbkdf2.add(btnPbkdfGenerateSalt);

		JLabel lblNewLabel_3_1_1_1_1 = new JLabel("Salt ops:");
		lblNewLabel_3_1_1_1_1.setBounds(10, 147, 71, 13);
		p_pbkdf2.add(lblNewLabel_3_1_1_1_1);

		JButton btnPbkdfSaveToFile = new JButton("Save to file");
		btnPbkdfSaveToFile.setBounds(228, 143, 113, 21);
		p_pbkdf2.add(btnPbkdfSaveToFile);

		JButton btnPbkdfLoadFromFile = new JButton("Load from file");
		btnPbkdfLoadFromFile.setBounds(372, 143, 113, 21);
		p_pbkdf2.add(btnPbkdfLoadFromFile);

		JButton btnPbkdfDeriveKey = new JButton("Derive key");
		btnPbkdfDeriveKey.setBounds(83, 185, 113, 21);
		p_pbkdf2.add(btnPbkdfDeriveKey);

		JLabel lblNewLabel_7_1_1 = new JLabel("Get key:");
		lblNewLabel_7_1_1.setBounds(10, 189, 63, 13);
		p_pbkdf2.add(lblNewLabel_7_1_1);

		JLabel lblNewLabel_6_1_1_1_1 = new JLabel("Desired key length: ");
		lblNewLabel_6_1_1_1_1.setBounds(10, 104, 113, 13);
		p_pbkdf2.add(lblNewLabel_6_1_1_1_1);

		JSpinner spnPbkdfDKlen = new JSpinner();
		spnPbkdfDKlen.setModel(new SpinnerNumberModel(new Integer(0), null, null, new Integer(1)));
		spnPbkdfDKlen.setBounds(133, 100, 63, 20);
		p_pbkdf2.add(spnPbkdfDKlen);

		JLabel lblNewLabel_6_1_1_1_1_1 = new JLabel("Iteration count:");
		lblNewLabel_6_1_1_1_1_1.setBounds(303, 104, 96, 13);
		p_pbkdf2.add(lblNewLabel_6_1_1_1_1_1);

		JSpinner spnPbkdfIterations = new JSpinner();
		spnPbkdfIterations.setBounds(409, 100, 63, 20);
		p_pbkdf2.add(spnPbkdfIterations);

		card_panel_buttons.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		card_panel_buttons.setBounds(226, 420, 607, 47);
		frmSaltHmacPbkdf.getContentPane().add(card_panel_buttons);

		JButton btnSaveOutputHmac = new JButton("Save generated HMAC (file)");
		btnSaveOutputHmac.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		card_panel_buttons.setLayout(button_card_layout);
		card_panel_buttons.add(btnSaveOutputHmac, "btn_hmac");

		JButton btnSaveOutputSalt = new JButton("Save salted password");
		card_panel_buttons.add(btnSaveOutputSalt, "btn_salt");

		JButton btnSaveOutputPbkdf = new JButton("Save derived key");
		card_panel_buttons.add(btnSaveOutputPbkdf, "btn_pbkdf");

		JButton btnSaveOutputHmacText = new JButton("Save generated HMAC (text)");
		card_panel_buttons.add(btnSaveOutputHmacText, "btn_hmac_text");

		// RADIO BUTTON GROUP
		ButtonGroup g = new ButtonGroup();

		radioButtons.add(rdbthSalt);
		radioButtons.add(rdbtnHmac);
		radioButtons.add(rdbtnPbkdf2);

		g.add(radioButtons.get(0));
		g.add(radioButtons.get(1));
		g.add(radioButtons.get(2));

		radioButtons.get(0).setActionCommand("p_salt");
		radioButtons.get(1).setActionCommand("p_hmac");
		radioButtons.get(2).setActionCommand("p_pbkdf2");

		radioButtons.get(1).setSelected(true);

		switchPanels(cardLayout, g, card_panel, card_panel_buttons);

		generateKey(btnSaltGenerate, txtSaltSalt, "salter");
		generateKey(btnHmacGenerateKey, txtHmacKey, "hmac");
		generateKey(btnHmacTextGenerateKey, txtHmacTextKey, "hmac");
		generateKey(btnPbkdfGenerateSalt, txtPbkdfSalt, "pbkdf");

		saveKeyToFile(btnSaltSaveToFile, txtSaltSalt);
		saveKeyToFile(btnHmacSaveToFile, txtHmacKey);
		saveKeyToFile(btnHmacTextSaveToFile, txtHmacTextKey);
		saveKeyToFile(btnPbkdfSaveToFile, txtPbkdfSalt);

		loadKeyFromFile(btnSaltLoadFromFile, txtSaltSalt);
		loadKeyFromFile(btnHmacLoadFromFile, txtHmacKey);
		loadKeyFromFile(btnHmacTextLoadFromFile, txtHmacTextKey);
		loadKeyFromFile(btnPbkdfLoadFromFile, txtPbkdfSalt);

		openFilePicker(btnUpload, textField);

		generateSalt(txtaOutput, btnSaltPassword, txtSaltPass, txtSaltSalt);
		saveOutputToFile(btnSaveOutputSalt, txtaOutput);

		generateHmacFromFile(txtaOutput, btnGenerateHmac, txtHmacKey);
		saveOutputToFile(btnSaveOutputHmac, txtaOutput);

		generateHmacFromText(txtaOutput, btnHmacTextGenerate, txtHmacText, txtHmacTextKey);
		saveOutputToFile(btnSaveOutputHmacText, txtaOutput);

		derivePbkdfKey(txtaOutput, btnPbkdfDeriveKey, txtPbkdfPassword, txtPbkdfSalt, spnPbkdfIterations,
				spnPbkdfDKlen);
		saveOutputToFile(btnSaveOutputPbkdf, txtaOutput);
	}

	private void switchPanels(CardLayout cl, ButtonGroup g, JPanel card_panel, JPanel card_panel_buttons) {
		for (JRadioButton b : radioButtons) {
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String card = g.getSelection().getActionCommand();

					txtaOutput.setText("");
					cl.show(card_panel, card);
					switchButtons(card, card_panel_buttons);

				}

			});
		}
	}

	private void switchButtons(String currentCard, JPanel btn_card_panel) {
		switch (currentCard) {
		case "p_salt":
			button_card_layout.show(btn_card_panel, "btn_salt");
			break;
		case "p_hmac":
			button_card_layout.show(btn_card_panel, "btn_hmac");
			break;
		case "p_pbkdf2":
			button_card_layout.show(btn_card_panel, "btn_pbkdf");
			break;
		case "text":
			button_card_layout.show(btn_card_panel, "btn_hmac_text");
			break;
		case "file":
			button_card_layout.show(btn_card_panel, "btn_hmac");
			break;

		}
	}

	private boolean validateKey(JTextField txtKey) {
		if (txtKey.getText().length() < 1) {
			JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "Key is empty	", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	public void generateKey(JButton btn, JTextField key, String keyFor) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				switch (keyFor) {
				case "salter":
					salter.generateRandomSalt();
					key.setText(Formating.byte2HexStr(salter.salt));
					break;
				case "hmac":
					hmac.generateRandomKey();
					key.setText(Formating.byte2HexStr(hmac.key));
					break;
				case "pbkdf":
					key.setText(Formating.byte2HexStr(pbkdf.genrateRandomSalt()));
					break;

				}
				// key.setText(Formating.byte2HexStr(keyBytes));

			}

		});
	}

	private void generateSalt(JTextArea txtaOutput, JButton btnGenerateOutput, JTextField salterPassword,
			JTextField salterSalt) {

		btnGenerateOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txtaOutput.setText(null);

				try {
					if (salterSalt.getText().length() < 1) {
						JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "No salt entered", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					salter.generateSaltFromString(salterSalt.getText());
					byte[] saltedPass = salter.saltPassword(salterPassword.getText());

					if (salterPassword.getText().length() == 0) {
						JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "No password to salt", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					txtaOutput.append("Salted password: ");
					txtaOutput.append(Formating.byte2HexStr(saltedPass));
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});
	}

	private void generateHmacFromFile(JTextArea txtaOutput, JButton btnGenerateOutput, JTextField hmacKey) {

		btnGenerateOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txtaOutput.setText(null);

				if (fileBytes == null) {
					JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "No file uploaded", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (hmacKey.getText().length() < 1) {
					JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "No key entered", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				hmac.generateKeyFromString(hmacKey.getText());

				byte[] generatedHmac = hmac.generateHMAC(fileBytes);

				txtaOutput.append("HMAC: ");
				txtaOutput.append(Formating.byte2HexStr(generatedHmac));

			}

		});
	}

	private void generateHmacFromText(JTextArea txtaOutput, JButton btnGenerateOutput, JTextField hmacPass,
			JTextField hmacKey) {
		btnGenerateOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txtaOutput.setText(null);

				if (hmacPass.getText().length() < 1) {
					JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "No input detected", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (hmacKey.getText().length() < 1) {
					JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "No key entered", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				hmac.generateKeyFromString(hmacKey.getText());

				byte[] generatedHmac = hmac.generateHMAC(hmacPass.getText().getBytes());

				txtaOutput.append("HMAC: ");
				txtaOutput.append(Formating.byte2HexStr(generatedHmac));

			}

		});
	}

	private void derivePbkdfKey(JTextArea txtaOutput, JButton btnGenerateOutput, JTextField pbkdfPass,
			JTextField pbkdfSalt, JSpinner iterationCount, JSpinner desiredKeyLength) {
		btnGenerateOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txtaOutput.setText(null);

				int iterations = (Integer) iterationCount.getValue();
				int dkLen = (Integer) desiredKeyLength.getValue();
				if (pbkdfPass.getText().length() < 1) {
					JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "No input detected", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (pbkdfSalt.getText().length() < 1) {
					JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "No salt entered", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (iterations < 1) {
					JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "Iteration count must be > 0", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (dkLen < 1) {
					JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "Desired key length must be > 0", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				byte[] derivedKey = pbkdf.deriveKey(pbkdfSalt.getText().getBytes(), pbkdfSalt.getText().getBytes(),
						iterations, dkLen);

				txtaOutput.append("Derived key as hex: ");
				txtaOutput.append(Formating.byte2HexStr(derivedKey));
				txtaOutput.append("\n");
				txtaOutput.append("\nDerived key as UTF-8 string: ");
				txtaOutput.append(new String(derivedKey));

			}

		});
	}

	private void saveKeyToFile(JButton btn, JTextField txtKey) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String key = txtKey.getText();

				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				if (!validateKey(txtKey))
					return;

				jfc.showSaveDialog(null);

				try {
					PrintWriter writer = new PrintWriter(jfc.getSelectedFile().toString() + ".txt");
					writer.println(key);
					writer.close();
				} catch (FileNotFoundException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

			}

		});
	}

	private void saveOutputToFile(JButton btn, JTextArea txtOutput) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String key = txtOutput.getText();

				if (key.length() < 1) {
					JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "No output detected", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				jfc.showSaveDialog(null);

				try {
					PrintWriter writer = new PrintWriter(jfc.getSelectedFile().toString() + ".txt");
					writer.println(key);
					writer.close();
				} catch (FileNotFoundException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

			}

		});
	}

	public void loadKeyFromFile(JButton btn, JTextField key) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.addChoosableFileFilter(filter);

				int f = jfc.showOpenDialog(null);

				if (f == JFileChooser.APPROVE_OPTION) {
					try {
						List<String> lines = Files.readAllLines(jfc.getSelectedFile().toPath());

						if (lines.size() != 1) {
							JOptionPane.showMessageDialog(frmSaltHmacPbkdf, "Wrong file", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						key.setText(lines.get(0));

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

		});
	}

	public void openFilePicker(JButton btn, JTextField txt) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				int r = jfc.showOpenDialog(null);

				if (r == JFileChooser.APPROVE_OPTION) {
					selectedFile = jfc.getSelectedFile();
					txt.setText(selectedFile.getAbsolutePath());
					try {
						fileBytes = Files.readAllBytes(selectedFile.toPath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}

		});
	}
}
