/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pemvismodul5.praktikum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author User
 */
public class Main extends javax.swing.JFrame {
    Connection conn;  //igunakan untuk menyimpan koneksi ke database
    private DefaultTableModel modelKaryawan, modelProyek, modelTransaksi; //untuk menyimpan data yang akan ditampilkan pada tabel
    ArrayList<Integer> listIdKaryawan = new ArrayList<>();
    ArrayList<Integer> listIdProyek = new ArrayList<>();
    //untuk menyimpan ID dari karyawan dan proyek,ARRAY ini memudahkan untuk pengambilan atau pemilihan ID terkait
    
    public Main() {
        initComponents();// untuk menginisialisasi komponen-komponen antarmuka
        initializeDatabaseConnection(); // untuk menginisialisasi koneksi ke database
        initializeTableModels(); //untuk menginisialisasi model tabel agar data dari database dapat dimasukkan ke dalam tabel yang sesuai
        initializeLoadData(); //untuk memuat data dari database dan memasukkannya ke dalam model tabel
        initializeCmbBoxTransaksi(); //untuk mengisi combo box yang berhubungan dengan transaksi
    }
    
    // Inisialisasi koneksi ke database
    private void initializeDatabaseConnection() {
        conn = pemvismodul5.praktikum.koneksi.getConnection();
    }
    
    // Inisialisasi model tabel
    private void initializeTableModels() {
        setupKaryawanTableModel();
        setupProyekTableModel();
        setupTransaksiTableModel();
    }
    
    // Inisialisasi load data
    private void initializeLoadData() {
        loadDataKaryawan();
        loadDataProyek();
        loadDataTransaksi();
    }
    
    // Initialize Combo box transaksi
    private void initializeCmbBoxTransaksi() {
        setupCmbBoxKaryawan();
        setupCmbBoxProyek();
    }
    
    private void setupKaryawanTableModel() { 
        modelKaryawan = new DefaultTableModel(); //untuk menampung data yang akan ditampilkan di tabe
        modelKaryawan.addColumn("ID"); //menambahkan colom"nya
        modelKaryawan.addColumn("Nama");
        modelKaryawan.addColumn("Jabatan");
        modelKaryawan.addColumn("Departemen");

        tbKaryawan.setModel(modelKaryawan); //mengaitkan modelKaryawan dengan JTable yang bernama tbKaryawan
    }
    
    private void setupProyekTableModel() {
        modelProyek = new DefaultTableModel();//untuk menampung data yang akan ditampilkan di tabe
        modelProyek.addColumn("ID"); //menambahkan colom"nya
        modelProyek.addColumn("Nama Proyek");
        modelProyek.addColumn("Durasi");

        tbProyek.setModel(modelProyek);//mengaitkan modelproyek dengan JTable yang bernama tbproyek
    }
    
    private void setupTransaksiTableModel() {
        modelTransaksi = new DefaultTableModel();
        modelTransaksi.addColumn("ID");
        modelTransaksi.addColumn("Karyawan");
        modelTransaksi.addColumn("Proyek");
        modelTransaksi.addColumn("Peran");

        tbTransaksi.setModel(modelTransaksi);
    }
    
    private void loadDataKaryawan() {
      modelKaryawan.setRowCount(0); //untuk mengosongkan seluruh data yang ada di dalam modelKaryawan

      try {
          String sql = "SELECT * FROM karyawan"; //mengambil semua data dari tabel karyawan di database.
          PreparedStatement ps = conn.prepareStatement(sql); //menjalankan query dan mengembalikan hasilnya dalam bentuk rs
          ResultSet rs = ps.executeQuery();//untuk menjalankan query SQL yang telah disiapkan dan mendapatkan hasilnya
          while (rs.next()) { //loop yang akan berjalan selama masih ada baris data dalam
            modelKaryawan.addRow(new Object[]{//menambahkan setiap baris data dari ResultSet ke dalam modelkaryawan
                rs.getInt("id"),
                rs.getString("nama"),
                rs.getString("jabatan"),
                rs.getString("dapartemen")
           });
            //mengambil nilai dari setiap kolom dalam baris saat ini pada ResultSet.
          }
      } catch (SQLException e) {
         System.out.println("Error Load Data Karyawan" + e.getMessage());
       }
    }
    
    private void loadDataProyek() {
      modelProyek.setRowCount(0);//untuk mengosongkan seluruh data yang ada di dalam modelproyek

      try {
          String sql = "SELECT * FROM proyek"; //mengambil semua data dari tabel proyek di database.
          PreparedStatement ps = conn.prepareStatement(sql);//menjalankan query dan mengembalikan hasilnya dalam bentuk rs
          ResultSet rs = ps.executeQuery(); //untuk menjalankan query SQL yang telah disiapkan dan mendapatkan hasilnya
          while (rs.next()) { ////loop yang akan berjalan selama masih ada baris data dalam rs
            modelProyek.addRow(new Object[]{ //menambahkan setiap baris data dari ResultSet ke dalam modelproyek
                rs.getInt("id"),
                rs.getString("nama_proyek"),
                rs.getString("durasi_pengerjaan") + " Minggu"
           }); ////mengambil nilai dari setiap kolom dalam baris saat ini pada ResultSet.
          }
      } catch (SQLException e) {
         System.out.println("Error Load Data Proyek" + e.getMessage());
       }
    }
    
    private void loadDataTransaksi() {
      modelTransaksi.setRowCount(0); //untuk mengosongkan seluruh data yang ada di dalam modelproyek

      try {
          String sql = "SELECT transaksi.id, transaksi.id_karyawan, karyawan.nama AS nama_karyawan, transaksi.id_proyek, proyek.nama_proyek, "
                  + "transaksi.peran FROM transaksi JOIN karyawan ON transaksi.id_karyawan = karyawan.id JOIN proyek ON "
                  + "transaksi.id_proyek = proyek.id;"; //menghubungkan tabel transaksi dengan tabel proyek
          PreparedStatement ps = conn.prepareStatement(sql);//menjalankan query dan mengembalikan hasilnya dalam bentuk rs
          ResultSet rs = ps.executeQuery(); //mengeksekusi query SQL tersebut dan mengembalikan hasilnya sebagai (rs)
          while (rs.next()) { //loop yang akan berjalan selama masih ada baris data dalam rs
            modelTransaksi.addRow(new Object[]{ //menambahkan setiap baris data dari ResultSet ke dalam modelTransaksi
                rs.getInt("transaksi.id"),
                rs.getString("nama_karyawan"),
                rs.getString("nama_proyek"),
                rs.getString("peran")
           }); //mengambil nilai dari setiap kolom dalam baris saat ini pada ResultSet.
          }
      } catch (SQLException e) {
         System.out.println("Error Load Data Transaksi" + e.getMessage());
       }
    }
    
    // Setup Combobox Karyawan - Tab Transaksi
    private void setupCmbBoxKaryawan() {
        cmbKaryawan.removeAllItems(); //menghapus semua item yang ada di dalam cmbKaryawan
        try {
            String query = "SELECT id, nama FROM karyawan";//membuat pernyataan SQL untuk mengambil kolom id,nama dri tabel karyawan
            PreparedStatement ps = conn.prepareStatement(query);//membuat PreparedStatement (ps) untuk menjalankan query SQL.
            ResultSet rs = ps.executeQuery();// menjalankan query dan menyimpan hasilnya di ResultSet (rs),

            while (rs.next()) { ////loop yang akan berjalan selama masih ada baris data dalam rs
                int id = rs.getInt("id");//mengambil nilai id karyawan dari baris saat ini di ResultSet.
                String nama = rs.getString("nama");//mengambil nilai nama karyawan dari baris saat ini di ResultSet
                listIdKaryawan.add(id);  // Simpan ID di ArrayList
                cmbKaryawan.addItem(nama); // hanya menambahkan nama ke ComboBox
            }
        } catch (SQLException e) {
            System.out.println("Error Query Combo box Karyawan" + e.getMessage());
        }
    }
    
    // Setup Combobox Proyek - Tab Transaksi
    private void setupCmbBoxProyek() {
        cmbProyek.removeAllItems();//menghapus semua item yang ada di dalam cmbproyek
        try {
            String query = "SELECT id, nama_proyek FROM proyek";//membuat pernyataan SQL untuk mengambil kolom id,nama dri tabel proyek
            PreparedStatement ps = conn.prepareStatement(query);//membuat PreparedStatement (ps) untuk menjalankan query SQL.
            ResultSet rs = ps.executeQuery();// menjalankan query dan menyimpan hasilnya di ResultSet (rs)

            while (rs.next()) { //loop yang akan berjalan selama masih ada baris data dalam rs
                int id = rs.getInt("id");
                String namaProyek = rs.getString("nama_proyek");
                listIdProyek.add(id);  // Simpan ID di ArrayList
                cmbProyek.addItem(namaProyek); // hanya menambahkan nama ke ComboBox
            }
        } catch (SQLException e) {
            System.out.println("Error Query Combo box Proyek" + e.getMessage());
        }
    }
    
    // Method validate textfield
    //untuk memvalidasi apakah sebuah JTextField (kolom teks) diisi atau tidak
    private boolean validateTextField(JTextField textField, String errorMessage) {
        if (textField.getText().isEmpty()) { //mengambil teks yang ada di dalam textField dn apakah teks trsbt kosong
            JOptionPane.showMessageDialog(this, errorMessage);
            return false; //gagal validasi
        }
        return true;//berhasil valifasi
    }
    
    
    // Karywan ==============================================================================
    // Insert / Save
    private void saveDataKaryawan() {// untk mnyimpn dta krywn
      if (!validateTextField(tfNamaKaryawan, "Nama Karyawan Harus diisi")) {//textf hrs diisi jka tdk akn mcl psn trsbt
         return;
      }
      
      if (!validateTextField(tfJabatanKaryawan, "Jabatan Karyawan Harus diisi")) {
         return;
      }
      
      if (!validateTextField(tfDapartemenKaryawan, "Dapartemen Karyawan Harus diisi")) {
         return;
      }
        
      try{
     //membuat query SQL untuk memasukkan data ke dalam tabel karyawan,dan data akn msk ke klom nma,jbtn,dprtmn,menampung nlai" dri dta
         String sql = "INSERT INTO karyawan (nama, jabatan, dapartemen) VALUES (?, ?, ?)";
         PreparedStatement ps = conn.prepareStatement(sql);//membuat PreparedStatement (ps) untuk menjalankan query SQL.
         ps.setString(1, tfNamaKaryawan.getText()); //mengambil teks yang dimasukkan oleh pengguna dalam kolom input nk
         ps.setString(2, tfJabatanKaryawan.getText());
         ps.setString(3, tfDapartemenKaryawan.getText());
         ps.executeUpdate();
         JOptionPane.showMessageDialog(this, "Data Karyawan saved successfully");
         loadDataKaryawan(); //untuk memperbarui data yang ditampilkan pada tabel
         
         tfNamaKaryawan.setText("");
         tfJabatanKaryawan.setText("");
         tfDapartemenKaryawan.setText("");
       } catch (SQLException e) {
         System.out.println("Error Save Data Karyawan" + e.getMessage());
       }
    }
    
    //Update
    private void updateDataKaryawan() { //mngubh dta krywn
      if (!validateTextField(tfIdKaryawan, "ID Karyawan Harus diisi")) { //textf hrs diisi jka tdk akn mcl psn trsbt
         return;
      }
      
      if (!validateTextField(tfNamaKaryawan, "Nama Karyawan Harus diisi")) {
         return;
      }
      
      if (!validateTextField(tfJabatanKaryawan, "Jabatan Karyawan Harus diisi")) {
         return;
      }
      
      if (!validateTextField(tfDapartemenKaryawan, "Dapartemen Karyawan Harus diisi")) {
         return;
      }
        
      try {
          String sql = "UPDATE karyawan SET nama = ?, jabatan = ?, dapartemen = ? WHERE id = ?";//memperbarui data
          PreparedStatement ps = conn.prepareStatement(sql); //membuat PreparedStatement (ps) untuk menjalankan query SQL.
          ps.setString(1, tfNamaKaryawan.getText());//mengambil teks yang dimasukkan oleh pengguna dalam kolom input nk
          ps.setString(2, tfJabatanKaryawan.getText());
          ps.setString(3, tfDapartemenKaryawan.getText());
          ps.setInt(4, Integer.parseInt(tfIdKaryawan.getText()));
          ps.executeUpdate();
          JOptionPane.showMessageDialog(this, "Data Karyawan updated successfully");
          loadDataKaryawan();//untuk memperbarui data yang ditampilkan pada tabel
          
          tfIdKaryawan.setText("");
          tfNamaKaryawan.setText("");
          tfJabatanKaryawan.setText("");
          tfDapartemenKaryawan.setText("");
      }  catch (SQLException e) {
          System.out.println("Error Update Data Karyawan" + e.getMessage());
      }
    }
    
    // Delete
    private void deleteDataKaryawan() {//menghapus data
      if (!validateTextField(tfIdKaryawan, "ID Karyawan Harus diisi")) { //textf hrs diisi jka tdk akn mcl psn trsbt
         return;
      }   
     
     try  {
          String sql = "DELETE FROM karyawan WHERE id = ?"; //menghps data pda tbl kryawan
          PreparedStatement ps = conn.prepareStatement(sql);  //membuat PreparedStatement (ps) untuk menjalankan query SQL.
          ps.setInt(1, Integer.parseInt(tfIdKaryawan.getText()));//mengmbalikn nilai ke string dn mnyimpn ke id kywn
          ps.executeUpdate();//untuk menjalankan pernyataan SQL yang mengubah data di database sprti insrt,dlt,updt
          JOptionPane.showMessageDialog(this, "Data karyawan deleted successfully");
          loadDataKaryawan();//untuk memperbarui data yang ditampilkan pada tabel
          
          tfIdKaryawan.setText("");
     } catch (SQLException e) {
          System.out.println("Error Deleted Data Karyawan" + e.getMessage());
      }
    }
    
    // Proyek ==================================================================
    // Insert / Save
    private void saveDataProyek() {  //menyimpan data proyek
      if (!validateTextField(tfNamaProyek, "Nama Proyek Harus diisi")) { //textf hrs diisi jka tdk akn mcl psn trsbt
         return;
      }
      if (!validateTextField(tfDurasiProyek, "Durasi Proyek Harus diisi")) {
         return;
      }
      
      try {
          try{
            String sql = "INSERT INTO proyek (nama_proyek, durasi_pengerjaan) VALUES (?, ?)";//memasukkan data baru ke dalam tabel proyek
            PreparedStatement ps = conn.prepareStatement(sql);//membuat PreparedStatement (ps) untuk menjalankan query SQL
            ps.setString(1, tfNamaProyek.getText());//mengambil teks yang dimasukkan oleh pengguna dalam kolom input nk
            ps.setInt(2, Integer.parseInt(tfDurasiProyek.getText()));//mengmbalikn nilai ke string dn mnyimpn ke id proyek
            ps.executeUpdate();//untuk menjalankan pernyataan SQL yang mengubah data di database sprti insrt,dlt,updt
            JOptionPane.showMessageDialog(this, "Data Proyek saved successfully");
            loadDataProyek(); //untuk memperbarui data yang ditampilkan pada tabel
            
            tfNamaProyek.setText("");
            tfDurasiProyek.setText("");
          } catch (SQLException e) {
            System.out.println("Error Save Data Proyek" + e.getMessage());
          }
      }catch(NumberFormatException ex) {
          JOptionPane.showMessageDialog(this, "Durasi yang anda inputkan bukan angka", "Durasi Proyek", JOptionPane.ERROR_MESSAGE);
      }
      
      
    }
    
    //Update
    private void updateDataProyek() { //memperbarui dta pryk
      if (!validateTextField(tfIdProyek, "ID Proyek Harus diisi")) { //textf hrs diisi jka tdk akn mcl psn trsbt
         return;
      } 
      if (!validateTextField(tfNamaProyek, "Nama Proyek Harus diisi")) {
         return;
      }
      if (!validateTextField(tfDurasiProyek, "Durasi Proyek Harus diisi")) {
         return;
      }
      
      try {
          try {
            String sql = "UPDATE proyek SET nama_proyek = ?, durasi_pengerjaan = ? WHERE id = ?"; //untuk memperbarui data
            PreparedStatement ps = conn.prepareStatement(sql);//membuat PreparedStatement (ps) untuk menjalankan query SQL
            ps.setString(1, tfNamaProyek.getText()); //mengambil teks yang dimasukkan oleh pengguna dalam kolom input np
            ps.setInt(2, Integer.parseInt(tfDurasiProyek.getText())); //mngemblikn nilai ke stringdan dn myimpn ke drsi pryk
            ps.setInt(3, Integer.parseInt(tfIdProyek.getText()));
            ps.executeUpdate();//untuk menjalankan pernyataan SQL yang mengubah data di database sprti insrt,dlt,updt
            JOptionPane.showMessageDialog(this, "Data Proyek updated successfully");
            loadDataProyek(); ////untuk memperbarui data yang ditampilkan pada tabel
            
            tfIdProyek.setText("");
            tfNamaProyek.setText("");
            tfDurasiProyek.setText("");
        }  catch (SQLException e) {
            System.out.println("Error Update Data Proyek" + e.getMessage());
        }
      }catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(this, "Durasi yang anda inputkan bukan angka", "Durasi Proyek", JOptionPane.ERROR_MESSAGE);
      }
    }
    
    // Delete
    private void deleteDataProyek() { //menghps dt pryk
        if (!validateTextField(tfIdProyek, "ID Proyek Harus diisi")) { //textf hrus diisi
            return;
        } 
     
        try  {
             String sql = "DELETE FROM proyek WHERE id = ?"; //mnghps dta id dri proyk
             PreparedStatement ps = conn.prepareStatement(sql);//membuat PreparedStatement (ps) untuk menjalankan query SQL
             ps.setInt(1, Integer.parseInt(tfIdProyek.getText())); //mengemblikn nili ke string dan mnyimpn ke id pryk
             ps.executeUpdate();//untuk menjalankan pernyataan SQL yang mengubah data di database sprti insrt,dlt,updt
             JOptionPane.showMessageDialog(this, "Data proyek deleted successfully");
             loadDataProyek(); //untuk memperbarui data yang ditampilkan pada tabel
             
             tfIdProyek.setText("");
        } catch (SQLException e) {
             System.out.println("Error Deleted Data Proyek" + e.getMessage());
        }
    }
    
    // Transaksi ==================================================================
    // Insert / Save
    private void saveDataTransaksi() { //mnyimpn dta trnsaksi
        int idKaryawan = cmbKaryawan.getSelectedIndex();
        int idProyek = cmbProyek.getSelectedIndex();
        
        try{
            //untuk menambah (insert) data baru ke dalam sebuah tabel di database
           String sql = "INSERT INTO transaksi (id_karyawan, id_proyek, peran) VALUES (?, ?, ?)";
           PreparedStatement ps = conn.prepareStatement(sql); //membuat PreparedStatement (ps) untuk menjalankan query SQL
           ps.setInt(1, listIdKaryawan.get(idKaryawan));
           ps.setInt(2, listIdProyek.get(idProyek));
           ps.setString(3, tfPeran.getText());
           ps.executeUpdate(); //untuk menjalankan pernyataan SQL yang mengubah data di database sprti insrt,dlt,updt
           JOptionPane.showMessageDialog(this, "Data Transaksi saved successfully");
           loadDataTransaksi();//untuk memperbarui data yang ditampilkan pada tabel
         } catch (SQLException e) {
           System.out.println("Error Save Data Transaksi" + e.getMessage());
         }
    }
    
    // Update 
    private void updateDataTransaksi() { //mngperbrui dt trnsk
        int idKaryawan = cmbKaryawan.getSelectedIndex();
        int idProyek = cmbProyek.getSelectedIndex();
        
      try {
          String sql = "UPDATE transaksi SET id_karyawan = ?, id_proyek = ?, peran = ? WHERE id = ?";
          PreparedStatement ps = conn.prepareStatement(sql);//membuat PreparedStatement (ps) untuk menjalankan query SQL
          ps.setInt(1, listIdKaryawan.get(idKaryawan));//untuk mengisi kolom ID karyawan dalam query SQL,
          ps.setInt(2, listIdProyek.get(idProyek));//
          ps.setString(3, tfPeran.getText());//untuk mengisi kolom yang menunjukkan peran dlm sql
          ps.setInt(4, Integer.parseInt(tfIdTransaksi.getText()));
          ps.executeUpdate();//untuk menjalankan pernyataan SQL yang mengubah data di database sprti insrt,dlt,updt
          JOptionPane.showMessageDialog(this, "Data Transaksi updated successfully");
          loadDataTransaksi(); //untuk memperbarui data yang ditampilkan pada tabel
      }  catch (SQLException e) {
          System.out.println("Error Update Data Transaksi" + e.getMessage());
      }
    }
    
    // Delete
    private void deleteDataTransaksi() { //mnghps dt trnsk
     try  {
          String sql = "DELETE FROM transaksi WHERE id = ?"; //menghpus id dri data transaksi
          PreparedStatement ps = conn.prepareStatement(sql);//membuat PreparedStatement (ps) untuk menjalankan query SQL
          ps.setInt(1, Integer.parseInt(tfIdTransaksi.getText())); //mengmblikn nili ke string dn didmpn ke id trnsksi
          ps.executeUpdate();//untuk menjalankan pernyataan SQL yang mengubah data di database sprti insrt,dlt,updt
          JOptionPane.showMessageDialog(this, "Data transaksi deleted successfully");
          loadDataTransaksi(); //untuk memperbarui data yang ditampilkan pada tabel
     } catch (SQLException e) {
          System.out.println("Error Deleted Data Transaksi" + e.getMessage());
      }
    }

  
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Transaksi = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfDapartemenKaryawan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfIdKaryawan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tfJabatanKaryawan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tfNamaKaryawan = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbKaryawan = new javax.swing.JTable();
        btnInsertKaryawan = new javax.swing.JButton();
        btnUpdateKaryawan = new javax.swing.JButton();
        btnDeleteKaryawan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfIdProyek = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tfNamaProyek = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tfDurasiProyek = new javax.swing.JTextField();
        btnInsertProyek = new javax.swing.JButton();
        btnUpdateProyek = new javax.swing.JButton();
        btnDeleteProyek = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProyek = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        tfPeran = new javax.swing.JTextField();
        btnInsertTransaksi = new javax.swing.JButton();
        btnUpdateTransaksi = new javax.swing.JButton();
        btnDeleteTransaksi = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbTransaksi = new javax.swing.JTable();
        cmbKaryawan = new javax.swing.JComboBox<>();
        cmbProyek = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        tfIdTransaksi = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 51, 51));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Square721 Cn BT", 1, 36)); // NOI18N
        jLabel1.setText("Karyawan");

        jLabel4.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel4.setText("ID");

        jLabel5.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel5.setText("Nama");

        jLabel6.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel6.setText("Jabatan");

        jLabel7.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel7.setText("Dapartemen");

        tbKaryawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbKaryawan);

        btnInsertKaryawan.setBackground(new java.awt.Color(0, 0, 0));
        btnInsertKaryawan.setFont(new java.awt.Font("Square721 Cn BT", 1, 14)); // NOI18N
        btnInsertKaryawan.setForeground(new java.awt.Color(255, 255, 255));
        btnInsertKaryawan.setText("Insert");
        btnInsertKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertKaryawanActionPerformed(evt);
            }
        });

        btnUpdateKaryawan.setBackground(new java.awt.Color(0, 0, 0));
        btnUpdateKaryawan.setFont(new java.awt.Font("Square721 Cn BT", 1, 14)); // NOI18N
        btnUpdateKaryawan.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateKaryawan.setText("Update");
        btnUpdateKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateKaryawanActionPerformed(evt);
            }
        });

        btnDeleteKaryawan.setBackground(new java.awt.Color(0, 0, 0));
        btnDeleteKaryawan.setFont(new java.awt.Font("Square721 Cn BT", 1, 14)); // NOI18N
        btnDeleteKaryawan.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteKaryawan.setText("Delete");
        btnDeleteKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteKaryawanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(btnInsertKaryawan))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tfNamaKaryawan, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .addComponent(tfJabatanKaryawan, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfDapartemenKaryawan)
                                    .addComponent(tfIdKaryawan))
                                .addGap(18, 18, 18))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnUpdateKaryawan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                .addComponent(btnDeleteKaryawan)
                                .addGap(18, 18, 18)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(339, 339, 339)
                        .addComponent(jLabel1)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(tfIdKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfNamaKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfJabatanKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tfDapartemenKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnInsertKaryawan)
                            .addComponent(btnUpdateKaryawan)
                            .addComponent(btnDeleteKaryawan)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(351, Short.MAX_VALUE))
        );

        Transaksi.addTab("Karyawan", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Square721 Cn BT", 1, 36)); // NOI18N
        jLabel2.setText("Proyek");

        jLabel8.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel8.setText("ID");

        jLabel9.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel9.setText("Nama Proyek");

        jLabel10.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel10.setText("Durasi");

        btnInsertProyek.setBackground(new java.awt.Color(0, 0, 0));
        btnInsertProyek.setFont(new java.awt.Font("Square721 Cn BT", 1, 14)); // NOI18N
        btnInsertProyek.setForeground(new java.awt.Color(255, 255, 255));
        btnInsertProyek.setText("Insert");
        btnInsertProyek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertProyekActionPerformed(evt);
            }
        });

        btnUpdateProyek.setBackground(new java.awt.Color(0, 0, 0));
        btnUpdateProyek.setFont(new java.awt.Font("Square721 Cn BT", 1, 14)); // NOI18N
        btnUpdateProyek.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateProyek.setText("Update");
        btnUpdateProyek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateProyekActionPerformed(evt);
            }
        });

        btnDeleteProyek.setBackground(new java.awt.Color(0, 0, 0));
        btnDeleteProyek.setFont(new java.awt.Font("Square721 Cn BT", 1, 14)); // NOI18N
        btnDeleteProyek.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteProyek.setText("Delete");
        btnDeleteProyek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProyekActionPerformed(evt);
            }
        });

        tbProyek.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tbProyek);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfIdProyek, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                    .addComponent(tfNamaProyek)
                                    .addComponent(tfDurasiProyek)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnInsertProyek)
                                .addGap(45, 45, 45)
                                .addComponent(btnUpdateProyek)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                                .addComponent(btnDeleteProyek)))
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(362, 362, 362))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(tfIdProyek))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tfNamaProyek, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(tfDurasiProyek))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnInsertProyek)
                            .addComponent(btnUpdateProyek)
                            .addComponent(btnDeleteProyek))
                        .addGap(302, 302, 302))))
        );

        Transaksi.addTab("Proyek", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 51, 51));

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Square721 Cn BT", 1, 36)); // NOI18N
        jLabel3.setText("Transaksi");

        jLabel11.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel11.setText("Karyawan");

        jLabel12.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel12.setText("Proyek");

        jLabel13.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel13.setText("Peran");

        btnInsertTransaksi.setBackground(new java.awt.Color(0, 0, 0));
        btnInsertTransaksi.setFont(new java.awt.Font("Square721 Cn BT", 1, 14)); // NOI18N
        btnInsertTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        btnInsertTransaksi.setText("Insert");
        btnInsertTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertTransaksiActionPerformed(evt);
            }
        });

        btnUpdateTransaksi.setBackground(new java.awt.Color(0, 0, 0));
        btnUpdateTransaksi.setFont(new java.awt.Font("Square721 Cn BT", 1, 14)); // NOI18N
        btnUpdateTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateTransaksi.setText("Update");
        btnUpdateTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateTransaksiActionPerformed(evt);
            }
        });

        btnDeleteTransaksi.setBackground(new java.awt.Color(0, 0, 0));
        btnDeleteTransaksi.setFont(new java.awt.Font("Square721 Cn BT", 1, 14)); // NOI18N
        btnDeleteTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteTransaksi.setText("Delete");
        btnDeleteTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTransaksiActionPerformed(evt);
            }
        });

        tbTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tbTransaksi);

        jLabel14.setFont(new java.awt.Font("Square721 Cn BT", 1, 18)); // NOI18N
        jLabel14.setText("ID");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnInsertTransaksi)
                                .addGap(45, 45, 45)
                                .addComponent(btnUpdateTransaksi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                .addComponent(btnDeleteTransaksi))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfPeran)
                                    .addComponent(cmbKaryawan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmbProyek, 0, 165, Short.MAX_VALUE)
                                    .addComponent(tfIdTransaksi))))
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(341, 341, 341))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14)
                            .addComponent(tfIdTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(cmbKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(cmbProyek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfPeran)
                            .addComponent(jLabel13))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnInsertTransaksi)
                            .addComponent(btnUpdateTransaksi)
                            .addComponent(btnDeleteTransaksi))))
                .addContainerGap())
        );

        Transaksi.addTab("Transaksi", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Transaksi)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Transaksi)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertKaryawanActionPerformed
        saveDataKaryawan();
        
        initializeCmbBoxTransaksi(); // update the combobox in the transaction tab when there is a data change (Insert, Update or Delete)
    }//GEN-LAST:event_btnInsertKaryawanActionPerformed

    private void btnUpdateKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateKaryawanActionPerformed
        updateDataKaryawan();
        
        initializeCmbBoxTransaksi(); // update the combobox in the transaction tab when there is a data change (Insert, Update or Delete)
    }//GEN-LAST:event_btnUpdateKaryawanActionPerformed

    private void btnDeleteKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteKaryawanActionPerformed
        deleteDataKaryawan();
        
        initializeCmbBoxTransaksi(); // update the combobox in the transaction tab when there is a data change (Insert, Update or Delete)
    }//GEN-LAST:event_btnDeleteKaryawanActionPerformed

    private void btnInsertProyekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertProyekActionPerformed
        saveDataProyek();
        
        initializeCmbBoxTransaksi(); // update the combobox in the transaction tab when there is a data change (Insert, Update or Delete)
    }//GEN-LAST:event_btnInsertProyekActionPerformed

    private void btnUpdateProyekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateProyekActionPerformed
        updateDataProyek();
        
        initializeCmbBoxTransaksi(); // update the combobox in the transaction tab when there is a data change (Insert, Update or Delete)
    }//GEN-LAST:event_btnUpdateProyekActionPerformed

    private void btnDeleteProyekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProyekActionPerformed
        deleteDataProyek();
        
        initializeCmbBoxTransaksi(); // update the combobox in the transaction tab when there is a data change (Insert, Update or Delete)
    }//GEN-LAST:event_btnDeleteProyekActionPerformed

    private void btnInsertTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertTransaksiActionPerformed
        saveDataTransaksi();
    }//GEN-LAST:event_btnInsertTransaksiActionPerformed

    private void btnUpdateTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateTransaksiActionPerformed
        updateDataTransaksi();
    }//GEN-LAST:event_btnUpdateTransaksiActionPerformed

    private void btnDeleteTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTransaksiActionPerformed
        deleteDataTransaksi();
    }//GEN-LAST:event_btnDeleteTransaksiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Transaksi;
    private javax.swing.JButton btnDeleteKaryawan;
    private javax.swing.JButton btnDeleteProyek;
    private javax.swing.JButton btnDeleteTransaksi;
    private javax.swing.JButton btnInsertKaryawan;
    private javax.swing.JButton btnInsertProyek;
    private javax.swing.JButton btnInsertTransaksi;
    private javax.swing.JButton btnUpdateKaryawan;
    private javax.swing.JButton btnUpdateProyek;
    private javax.swing.JButton btnUpdateTransaksi;
    private javax.swing.JComboBox<String> cmbKaryawan;
    private javax.swing.JComboBox<String> cmbProyek;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tbKaryawan;
    private javax.swing.JTable tbProyek;
    private javax.swing.JTable tbTransaksi;
    private javax.swing.JTextField tfDapartemenKaryawan;
    private javax.swing.JTextField tfDurasiProyek;
    private javax.swing.JTextField tfIdKaryawan;
    private javax.swing.JTextField tfIdProyek;
    private javax.swing.JTextField tfIdTransaksi;
    private javax.swing.JTextField tfJabatanKaryawan;
    private javax.swing.JTextField tfNamaKaryawan;
    private javax.swing.JTextField tfNamaProyek;
    private javax.swing.JTextField tfPeran;
    // End of variables declaration//GEN-END:variables
}