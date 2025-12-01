package Controller;

import Model.Mahasiswa;
import Model.PendidikanDAO;
import View.Dashboard_Mahasiswa;
import View.Profile_Mahasiswa;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

public class ProfileController {
    
    private Profile_Mahasiswa view;
    private Mahasiswa mhs;
    private PendidikanDAO dao;

    public ProfileController(Profile_Mahasiswa view, Mahasiswa mhs) {
        this.view = view;
        this.mhs = mhs;
        this.dao = new PendidikanDAO();
        
        // Sambungkan Controller ke View
        view.setController(this);
        
        // Isi data awal & Kunci form
        isiFormulir();
        view.setEditMode(false); 
    }
    
    public void isiFormulir() {
        if (mhs == null) return;
        
        // Data Akademik
        view.setNama(mhs.getNama());
        view.setNim(mhs.getNim());
        view.setProdi(mhs.getProdi());
        view.setSemester(String.valueOf(mhs.getSemesterMasuk()));
        
        // Data Pribadi
        view.setEmail(mhs.getEmail());
        view.setNoHp(mhs.getNoHp());
        view.setAlamat(mhs.getAlamat());
        view.setTempatLahir(mhs.getTempatLahir());
        view.setJenisKelamin(mhs.getJk());
        
        // Data Tanggal Lahir (Format: yyyy-MM-dd)
        if (mhs.getTanggalLahir() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            view.setTanggalLahir(sdf.format(mhs.getTanggalLahir()));
        } else {
            view.setTanggalLahir("");
        }
        
        // Data Bank
        view.setBank(mhs.getBank());
        view.setRekening(mhs.getNoRekening());
        view.setAtasNama(mhs.getNamaRekening());
    }

    // LOGIKA TOMBOL 

    public void klikEdit() {
        view.setEditMode(true);
        JOptionPane.showMessageDialog(view, "Silakan ubah data");
    }
    
    public void klikBatal() {
        view.setEditMode(false);
        isiFormulir(); 
    }
    
    public void klikSimpan() {
        try {
            // 1. Ambil Data Inputan
            mhs.setEmail(view.getInputEmail());
            mhs.setNoHp(view.getInputNoHp());
            mhs.setAlamat(view.getInputAlamat());
            mhs.setTempatLahir(view.getInputTempatLahir());
            mhs.setJk(view.getInputJenisKelamin());
            
            mhs.setBank(view.getInputBank());
            mhs.setNoRekening(view.getInputRekening());
            mhs.setNamaRekening(view.getInputAtasNama());
            
            // 2. Parsing Tanggal Lahir
            String tglString = view.getInputTanggalLahir();
            if (tglString != null && !tglString.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                mhs.setTanggalLahir(sdf.parse(tglString));
            }
            
            // 3. Update ke Database
            boolean sukses = dao.updateProfilMahasiswa(mhs);
            
            if (sukses) {
                JOptionPane.showMessageDialog(view, "Data Profil Berhasil Disimpan!");
                view.setEditMode(false);
            } else {
                JOptionPane.showMessageDialog(view, "Gagal menyimpan data ke Database.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Gagal! Cek Format Tanggal (yyyy-MM-dd)");
        }
    }

    public void kembali() {
        view.dispose();
        Dashboard_Mahasiswa dashboard = new Dashboard_Mahasiswa();
        new MahasiswaController(dashboard, mhs);
        dashboard.setVisible(true);
    }
}