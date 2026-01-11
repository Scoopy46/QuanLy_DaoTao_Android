using Microsoft.AspNetCore.Mvc;
using StudentManager.DTOs;
using StudentManager.Models;
using StudentManager.Services;

namespace StudentManager.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Produces("application/json")]
    public class DiemThiController : ControllerBase
    {
        private readonly IDiemThiService _service;
        private readonly ILogger<DiemThiController> _logger;

        public DiemThiController(IDiemThiService service, ILogger<DiemThiController> logger)
        {
            _service = service ?? throw new ArgumentNullException(nameof(service));
            _logger = logger ?? throw new ArgumentNullException(nameof(logger));
        }

        /// <summary>
        /// Lấy danh sách điểm thi theo lớp và/hoặc mã môn học
        /// </summary>
        /// <param name="lop">Lớp cần lọc (để trống = tất cả)</param>
        /// <param name="maMH">Mã môn học cần lọc (để trống = tất cả)</param>
        /// <returns>Danh sách điểm thi</returns>
        [HttpGet]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public IActionResult Get([FromQuery] string? lop, [FromQuery] string? maMH)
        {
            try
            {
                var result = _service.GetByFilter(lop, maMH);
                return Ok(result);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi lấy danh sách điểm thi");
                return StatusCode(500, "Đã xảy ra lỗi khi xử lý yêu cầu");
            }
        }

        /// <summary>
        /// Tạo mới hoặc cập nhật điểm thi
        /// </summary>
        /// <param name="dto">Thông tin điểm thi</param>
        /// <returns>Kết quả thao tác</returns>
        [HttpPost]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public async Task<IActionResult> CreateOrUpdate([FromBody] DiemThiDTO dto)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            try
            {
                // Validate foreign keys
                var isValidSV = await _service.ValidateMaSVAsync(dto.MaSV);
                var isValidMH = await _service.ValidateMaMHAsync(dto.MaMH);

                if (!isValidSV)
                {
                    return BadRequest(new { message = "Mã sinh viên không tồn tại" });
                }

                if (!isValidMH)
                {
                    return BadRequest(new { message = "Mã môn học không tồn tại" });
                }

                var diemThi = new DiemThi
                {
                    MaSV = dto.MaSV,
                    MaMH = dto.MaMH,
                    DiemLan1 = dto.DiemLan1,
                    DiemLan2 = dto.DiemLan2
                };

                var success = await _service.CreateOrUpdateAsync(diemThi);
                
                if (!success)
                {
                    return BadRequest(new { message = "Không thể tạo/cập nhật điểm thi" });
                }

                return Ok(new { message = "Thành công", data = diemThi });
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Lỗi khi tạo/cập nhật điểm thi");
                return StatusCode(500, "Đã xảy ra lỗi khi xử lý yêu cầu");
            }
        }
    }
}

