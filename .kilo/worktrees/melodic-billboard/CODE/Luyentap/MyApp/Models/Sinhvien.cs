using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

public class SinhVien
{
    
    [Key]
    [StringLength(6, ErrorMessage = "MSSV dài tối đa 6 ký tự.")]
    [RegularExpression(@"^[A-Z]\d{4}[FM]$", ErrorMessage = "MSSV không đúng định dạng (1 chữ cái đầu mã khoa, 4 ký tự số, cuối là F hoặc M).")]
    public string Mssv { get; set; } = string.Empty;

    [Required(ErrorMessage = "Họ và tên không được để trống.")]
    public string Ten { get; set; } = string.Empty;

    [Range(0, 1, ErrorMessage = "PhaiNu chỉ nhận giá trị 0 (Nam) hoặc 1 (Nữ).")]
    public int PhaiNu { get; set; }

    [Required(ErrorMessage = "Địa chỉ không được để trống.")]
    public string? DiaChi { get; set; }

    [RegularExpression(@"^(\d{8}|\d{10})?$", ErrorMessage = "Điện thoại phải gồm đúng 8 hoặc 10 ký tự số.")]
    public string? DienThoai { get; set; }

    [Required(ErrorMessage = "Mã khoa không được để trống.")]
    public string MaKhoa { get; set; } = string.Empty;

    // [Index(IsUnique = true)]
    public string? SoCmnd { get; set; }

    [DataType(DataType.Date)]
    public DateTime? NgaySinh { get; set; }

    [Required(ErrorMessage = "Ngày nhập học không được để trống.")]
    [DataType(DataType.Date)]
    public DateTime NgayNhapHoc { get; set; }

    [DataType(DataType.Date)]
    public DateTime? NgayVaoDoan { get; set; }

    [DataType(DataType.Date)]
    public DateTime? NgayVaoDang { get; set; }

    [DataType(DataType.Date)]
    public DateTime? NgayRaTruong { get; set; }

    public string? LyDoNgungHoc { get; set; }
    [ForeignKey("MaKhoa")]
    public virtual Khoa? Khoa { get; set; }

    public virtual ICollection<KetQua> KetQuas { get; set; } = new List<KetQua>();
}