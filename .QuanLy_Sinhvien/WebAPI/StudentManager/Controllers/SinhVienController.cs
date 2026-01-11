using Microsoft.AspNetCore.Mvc;
using StudentManager.Models;
using StudentManager.Services;

namespace StudentManager.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class SinhVienController : ControllerBase
    {
        private readonly ISinhVienService _service;
        public SinhVienController(ISinhVienService service) { _service = service; }

        [HttpGet]
        public IActionResult GetAll() => Ok(_service.GetAll());

        [HttpGet("{id}")]
        public IActionResult Get(string id)
        {
            var sv = _service.GetById(id);
            if (sv == null) return NotFound();
            return Ok(sv);
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] SinhVien sv)
        {
            var ok = await _service.CreateAsync(sv);
            if (!ok) return BadRequest("Không thể tạo (có thể mã đã tồn tại).");
            return CreatedAtAction(nameof(Get), new { id = sv.MaSV }, sv);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(string id, [FromBody] SinhVien sv)
        {
            var ok = await _service.UpdateAsync(id, sv);
            if (!ok) return NotFound();
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(string id)
        {
            var ok = await _service.DeleteAsync(id);
            if (!ok) return NotFound();
            return NoContent();
        }
    }
}