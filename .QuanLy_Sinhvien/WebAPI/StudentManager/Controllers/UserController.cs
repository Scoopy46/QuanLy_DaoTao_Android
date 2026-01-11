using Microsoft.AspNetCore.Mvc;
using StudentManager.Models;
using StudentManager.Services;

namespace StudentManager.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UserController : ControllerBase
    {
        private readonly IUserService _service;
        public UserController(IUserService service) { _service = service; }

        [HttpGet]
        public IActionResult GetAll() => Ok(_service.GetAll());

        [HttpGet("{id}")]
        public IActionResult Get(int id)
        {
            var u = _service.GetById(id);
            if (u == null) return NotFound();
            return Ok(u);
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] User u)
        {
            var ok = await _service.CreateAsync(u);
            if (!ok) return BadRequest("Không thể tạo user (có thể username đã tồn tại).");
            return CreatedAtAction(nameof(Get), new { id = u.UserID }, u);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, [FromBody] User u)
        {
            var ok = await _service.UpdateAsync(id, u);
            if (!ok) return NotFound();
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            var ok = await _service.DeleteAsync(id);
            if (!ok) return NotFound();
            return NoContent();
        }
    }
}