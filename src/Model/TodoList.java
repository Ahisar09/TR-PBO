package Model;

import java.util.Date;

public class TodoList {
    private int idTodo;
    private int idMahasiswa;
    private String judul;
    private String deskripsi;
    private String prioritas; 
    private Date deadline;
    private String status;    
    private Date dibuatPada;

    public TodoList() {}

    // Getter & Setter
    public int getIdTodo() { return idTodo; }
    public void setIdTodo(int idTodo) { this.idTodo = idTodo; }

    public int getIdMahasiswa() { return idMahasiswa; }
    public void setIdMahasiswa(int idMahasiswa) { this.idMahasiswa = idMahasiswa; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getPrioritas() { return prioritas; }
    public void setPrioritas(String prioritas) { this.prioritas = prioritas; }

    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getDibuatPada() { return dibuatPada; }
    public void setDibuatPada(Date dibuatPada) { this.dibuatPada = dibuatPada; }
}