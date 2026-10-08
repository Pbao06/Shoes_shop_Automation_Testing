using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

public class GiangDay
{
    [Key]
    [StringLength(4, ErrorMessage = "MaKhoaHoc gồm 4 ký tự.")]
    [RegularExpression(@"^K\d{3}$", ErrorMessage = "MaKhoaHoc phải bắt đầu bằng chữ 'K' và 3 ký tự số tiếp theo.")]
    public string MaKhoaHoc { get; set; } = string.Empty;

    [Required(ErrorMessage = "Mã giáo viên không được để trống.")]
    public string MaGv { get; set; } = string.Empty;

    [Required(ErrorMessage = "Mã môn học không được để trống.")]
    public string MaMh { get; set; } = string.Empty;

    [Range(1, 3, ErrorMessage = "HocKy chỉ nhận giá trị 1, 2 hoặc 3.")]
    public int HocKy { get; set; }

    [Required(ErrorMessage = "Niên khóa không được để trống.")]
    public string NienKhoa { get; set; } = string.Empty;

    [DataType(DataType.Date)]
    public DateTime? NgayBatDauLyThuyet { get; set; }

    [DataType(DataType.Date)]
    public DateTime? NgayBatDauThuHanh { get; set; }

    [DataType(DataType.Date)]
    public DateTime? NgayKetThuc { get; set; }

    [Range(2, 6, ErrorMessage = "TongSoTC phải nằm trong khoảng từ 2 đến 6.")]
    public int TongSoTc { get; set; }

    [Range(0, int.MaxValue, ErrorMessage = "SoTCLT phải lớn hơn hoặc bằng 0.")]
    public int Sotclt { get; set; } = 0;

    [Range(0, int.MaxValue, ErrorMessage = "SoTCTH phải lớn hơn hoặc bằng 0.")]
    public int Sotcth { get; set; } = 0;

    [Range(0, int.MaxValue)]
    public int SoTietLt { get; set; }

    [Range(0, int.MaxValue)]
    public int SoTietTh { get; set; }
    // Navigation Properties
    [ForeignKey("MaGv")]
    public virtual GiaoVien? GiaoVien { get; set; }

    [ForeignKey("MaMh")]
    public virtual MonHoc? MonHoc { get; set; }

    public virtual ICollection<KetQua> KetQuas { get; set; } = new List<KetQua>();
}