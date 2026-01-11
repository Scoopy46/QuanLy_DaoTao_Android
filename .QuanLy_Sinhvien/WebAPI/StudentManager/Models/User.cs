using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace StudentManager.Models
{
    [Table("[User]")]
    public class User
    {
        [Key]
        public int UserID { get; set; }

        [Required, StringLength(50)]
        public string UserName { get; set; }

        [Required, StringLength(100)]
        public string PassWord { get; set; }

        [StringLength(50)]
        public string FullName { get; set; }

        [StringLength(10)]
        public string Type { get; set; }

        [StringLength(20)]
        public string Lop { get; set; }
    }
}