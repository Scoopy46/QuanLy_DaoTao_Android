using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace StudentManager.Models
{
    [Table("SinhVien")]
    public class SinhVien
    {
        [Key, StringLength(10)]
        public string MaSV { get; set; }

        [Required, StringLength(50)]
        public string HoTen { get; set; }

        public int NamSinh { get; set; }

        [StringLength(20)]
        public string Lop { get; set; }

        [StringLength(255)]
        public string Anh { get; set; }
    }
}
