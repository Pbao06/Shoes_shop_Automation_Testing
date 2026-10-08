using System.ComponentModel.DataAnnotations;

public class MonHoc
{
    [Key]
    [Required(ErrorMessage = "Mã môn học không được để trống.")]
    public string MaMh { get; set; } = string.Empty;

    [Required(ErrorMessage = "Tên môn học không được để trống.")]
    public string TenMh { get; set; } = string.Empty;

    [Range(2, int.MaxValue, ErrorMessage = "Số tín chỉ tối thiểu (SoTC_ToiThieu) phải lớn hơn hoặc bằng 2.")]
    public int SotcToiThieu { get; set; }
    // Navigation Properties
    public virtual ICollection<GiangDay> GiangDays { get; set; } = new List<GiangDay>();
}