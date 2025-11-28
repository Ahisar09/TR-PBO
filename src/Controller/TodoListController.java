package Controller;

import Model.Mahasiswa;
import Model.PendidikanDAO;
import Model.TodoList;
import View.Dashboard_Mahasiswa;
import View.ToDo_List; 
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TodoListController {
    
    private ToDo_List view;
    private Mahasiswa mhs;
    private PendidikanDAO dao;
    
    // List bantu untuk menyimpan ID Todo yang sedang tampil di tabel
    private List<TodoList> listTodoSedangTampil;

    public TodoListController(ToDo_List view, Mahasiswa mhs) {
        this.view = view;
        this.mhs = mhs;
        this.dao = new PendidikanDAO();
        
        view.setController(this); // Sambungkan
        refreshTabel();
    }
    
    // --- 1. REFRESH TABEL (TAMPILKAN DATA) ---
    public void refreshTabel() {
        listTodoSedangTampil = dao.getTodoList(mhs.getIdMahasiswa());
        
        String[] kolom = {"Judul", "Deadline", "Prioritas", "Status", "Deskripsi"};
        DefaultTableModel model = new DefaultTableModel(null, kolom);
        
        // Format tanggal hanya untuk TAMPILAN di Tabel (Bukan untuk input/simpan)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (TodoList t : listTodoSedangTampil) {
            Object[] row = {
                t.getJudul(),
                (t.getDeadline() != null) ? sdf.format(t.getDeadline()) : "-",
                t.getPrioritas(),
                t.getStatus(),
                t.getDeskripsi()
            };
            model.addRow(row);
        }
        view.setTabelTodo(model);
    }
    
    // --- 2. KLIK TABEL (POPULATE FORM) ---
    public void klikTabel() {
        int row = view.getTableTodo().getSelectedRow();
        if (row != -1) {
            // Ambil data dari list memori
            TodoList t = listTodoSedangTampil.get(row);
            
            // Masukkan ke Form View
            view.setJudul(t.getJudul());
            view.setDeskripsi(t.getDeskripsi());
            view.setPrioritas(t.getPrioritas());
            view.setStatus(t.getStatus());
            
            // --- BAGIAN DATE CHOOSER ---
            // Langsung set objek Date ke JDateChooser (Tidak perlu parsing string)
            view.setDeadline(t.getDeadline()); 
        }
    }
    
    // --- 3. LOGIKA TOMBOL TAMBAH ---
    public void tambah() {
        // Validasi: Pastikan tanggal dipilih
        if (view.getInputDeadline() == null) {
            JOptionPane.showMessageDialog(view, "Tanggal Deadline belum dipilih!");
            return;
        }

        try {
            TodoList t = new TodoList();
            t.setIdMahasiswa(mhs.getIdMahasiswa());
            t.setJudul(view.getInputJudul());
            t.setDeskripsi(view.getInputDeskripsi());
            t.setPrioritas(view.getInputPrioritas());
            t.setStatus(view.getInputStatus()); 
            
            // --- INPUT DATE CHOOSER ---
            // Langsung ambil Date dari View
            t.setDeadline(view.getInputDeadline()); 
            
            if (dao.tambahTodo(t)) {
                JOptionPane.showMessageDialog(view, "Berhasil Menambah Tugas!");
                refreshTabel();
                view.resetForm(); // Kosongkan form setelah simpan
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Gagal menambah data.");
        }
    }
    
    // --- 4. LOGIKA TOMBOL UPDATE ---
    public void update() {
        int row = view.getTableTodo().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih tugas di tabel untuk diedit!");
            return;
        }
        
        if (view.getInputDeadline() == null) {
            JOptionPane.showMessageDialog(view, "Tanggal Deadline tidak boleh kosong!");
            return;
        }
        
        try {
            // Ambil ID dari list yang tersimpan (Data Lama)
            TodoList tLama = listTodoSedangTampil.get(row);
            
            // Buat Objek Baru dengan ID yang sama
            TodoList tBaru = new TodoList();
            tBaru.setIdTodo(tLama.getIdTodo()); // ID Tetap
            tBaru.setJudul(view.getInputJudul());
            tBaru.setDeskripsi(view.getInputDeskripsi());
            tBaru.setPrioritas(view.getInputPrioritas());
            tBaru.setStatus(view.getInputStatus());
            
            // --- INPUT DATE CHOOSER ---
            tBaru.setDeadline(view.getInputDeadline());
            
            if (dao.updateTodo(tBaru)) {
                JOptionPane.showMessageDialog(view, "Data Berhasil Diupdate!");
                refreshTabel();
                view.resetForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Gagal Update.");
        }
    }

    // --- 5. LOGIKA TOMBOL HAPUS ---
    public void hapus() {
        int row = view.getTableTodo().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih tugas yang mau dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(view, "Yakin hapus tugas ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            TodoList t = listTodoSedangTampil.get(row);
            if (dao.hapusTodo(t.getIdTodo())) {
                JOptionPane.showMessageDialog(view, "Tugas Dihapus.");
                refreshTabel();
                view.resetForm();
            }
        }
    }
    
    // --- 6. LOGIKA TOMBOL TANDAI SELESAI ---
    public void tandaiSelesai() {
        int row = view.getTableTodo().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih tugas yang ingin ditandai selesai!");
            return;
        }
        
        TodoList t = listTodoSedangTampil.get(row);
        
        // Update Status jadi "selesai"
        boolean sukses = dao.updateStatusTodo(t.getIdTodo(), "selesai");
        
        if (sukses) {
            JOptionPane.showMessageDialog(view, "Selamat! Tugas telah diselesaikan.");
            refreshTabel();
            view.setStatus("selesai"); 
        } else {
            JOptionPane.showMessageDialog(view, "Gagal mengupdate status.");
        }
    }
    
    // --- 7. LOGIKA KEMBALI ---
    public void kembali() {
        javax.swing.SwingUtilities.getWindowAncestor(view).dispose();
        Dashboard_Mahasiswa dashboard = new Dashboard_Mahasiswa();
        new MahasiswaController(dashboard, mhs);
        dashboard.setVisible(true);
    }
}