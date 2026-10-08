using System.Text.Json;
using Data;
using Microsoft.EntityFrameworkCore;

public class LuyenTap
{
    // dang ki DI 
    private readonly AppDbContext _context;
    public LuyenTap(AppDbContext context) => _context = context;
    public void Cau1()
    {
        var rs = _context.SinhViens.Include(sv => sv.KetQuas).AsEnumerable();
        var cau36 = rs.Select(sv => new
        {
            Masv = sv.Mssv,
            DiaChi = sv.DiaChi,
            DiemTb = sv.KetQuas.Any() ? sv.KetQuas.Average(kq => kq.DiemKhoaHoc) : 0
        }).ToList();
        foreach (var sv in cau36)
        {
            Console.WriteLine($" {sv.Masv} - {sv.DiaChi} - {sv.DiemTb}");
        }
    }
}




