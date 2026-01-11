using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace StudentManager.Models
{
    [Table("DiemThi")]
    public class DiemThi
    {
        [Key, Column(Order = 0), StringLength(10)]
        public string MaSV { get; set; }

        [Key, Column(Order = 1), StringLength(10)]
        public string MaMH { get; set; }

        [Column(TypeName = "decimal(4,2)")]
        public decimal? DiemLan1 { get; set; }

        [Column(TypeName = "decimal(4,2)")]
        public decimal? DiemLan2 { get; set; }

        // Navigation properties
        [ForeignKey("MaSV")]
        public SinhVien SinhVien { get; set; }

        [ForeignKey("MaMH")]
        public MonHoc MonHoc { get; set; }
    }
}

