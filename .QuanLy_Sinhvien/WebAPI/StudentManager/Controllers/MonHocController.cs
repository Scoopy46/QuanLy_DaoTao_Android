using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StudentManager.Data;
using StudentManager.Models;

namespace StudentManager.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class MonHocController : ControllerBase
    {
        private readonly SinhVienDbContext _db;
        public MonHocController(SinhVienDbContext db) { _db = db; }

        [HttpGet]
        public IActionResult GetAll()
        {
            var monHocList = _db.MonHoc.AsNoTracking().ToList();
            return Ok(monHocList);
        }
    }
}

