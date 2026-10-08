using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

public class KetQua
{
    [Key, Column(Order = 0)]
    [StringLength(6)]
    public string Mssv { get; set; } = string.Empty;

    [Key, Column(Order = 1)]
    [StringLength(4)]
    public string MaKhoaHoc { get; set; } = string.Empty;

    [Range(0, 10, ErrorMessage = "DiemKTGiuaKy phải nằm trong khoảng từ 0 đến 10.")]
    public double DiemKtGiuaKy { get; set; } = 0;

    [Range(0, 10, ErrorMessage = "DiemThiLan1 phải nằm trong khoảng từ 0 đến 10.")]
    public double DiemThiLan1 { get; set; } = 0;

    [Range(0, 10, ErrorMessage = "DiemThiLan2 phải nằm trong khoảng từ 0 đến 10.")]
    public double? DiemThiLan2 { get; set; }

    // Thuộc tính tính toán điểm khóa học theo công thức yêu cầu
    [NotMapped] // Không lưu trực tiếp xuống cột trong DB nếu dùng code-first tính động, hoặc có thể dùng computed column
    public double DiemKhoaHoc
    {
        get
        {
            double maxThi = Math.Max(DiemThiLan1, DiemThiLan2 ?? 0);
            return (0.4 * DiemKtGiuaKy) + (0.6 * maxThi);
        }
    }
    // Navigation Properties
    [ForeignKey("Mssv")]
    public virtual SinhVien? SinhVien { get; set; }


    [ForeignKey("MaKhoaHoc")]
    public virtual GiangDay? GiangDay { get; set; }
}