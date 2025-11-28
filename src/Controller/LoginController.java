package Controller;

// ======================
// IMPORT DI TARUH DI SINI
// ======================
import Model.PendidikanDAO;
import Model.Mahasiswa;

import View.Login;
import View.Dashboard_Mahasiswa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ======================
// CLASS DIMULAI DI SINI
// ======================
public class LoginController {

    private Login view;
    private PendidikanDAO dao;

    public LoginController(Login view) {
        this.view = view;
        this.dao = new PendidikanDAO();

        initController();
    }

    private void initController() {
        view.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesLogin();
            }
        });

        view.getPasswordField().addActionListener(e -> prosesLogin());
    }

    private void prosesLogin() {
        String username = view.getUsernameInput();
        String password = view.getPasswordInput();

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            view.showMessage("Username dan Password tidak boleh kosong!");
            return;
        }

        Mahasiswa mhs = dao.login(username, password);

        if (mhs != null) {
            view.showMessage("Login Berhasil! Selamat Datang, " + mhs.getNama());
            view.dispose();
            bukaDashboardMahasiswa(mhs);
        } else {
            view.showMessage("Login Gagal! Username atau Password salah.");
        }
    }

    private void bukaDashboardMahasiswa(Mahasiswa mhs) {
        Dashboard_Mahasiswa dashboardView = new Dashboard_Mahasiswa();
        new MahasiswaController(dashboardView, mhs);
        dashboardView.setVisible(true);
    }
}
