# Recipe Share — Nền tảng chia sẻ công thức nấu ăn

Ứng dụng mạng xã hội chia sẻ công thức nấu ăn: người dùng đăng công thức → Admin duyệt → Hiển thị công khai.

## Kiến trúc (theo sơ đồ)

- **Frontend (Recipe app)**: Giao diện web, màu vàng, theme nhà bếp.
- **JWT**: Xác thực giữa client và backend.
- **Public Access**: Xem công thức không cần đăng nhập.
- **Recipes Service**: Quản lý công thức, upload, duyệt.
- **Cheff Service**: Quản lý người dùng (Chef), vai trò.
- **Image Processing Service**: Xử lý ảnh (resize, lưu).
- **Database**: H2 (Chefs, Recipes).

## Quyền (Roles)

| Vai trò   | Mô tả |
|-----------|--------|
| **Viewer** | Chỉ xem công thức đã duyệt. |
| **User**   | Đăng công thức (chờ admin duyệt), xem công thức của mình. |
| **Admin**  | Duyệt/từ chối công thức, quản trị. |

## Công nghệ

- **Backend**: Java 17, Spring Boot 3.2, Spring Security, JWT, JPA/H2, Thymeleaf.
- **Frontend**: HTML/CSS (màu vàng, theme nhà bếp), Thymeleaf.

## Chạy ứng dụng

```bash
mvn spring-boot:run
```

Truy cập: **http://localhost:8080**

### Tài khoản mặc định (tự tạo khi lần chạy đầu)

- **Admin**: `admin` / `admin123`

### Đăng ký

- Vào **Đăng ký** → Chọn quyền **Người xem** hoặc **Người dùng** → Sau khi đăng ký có thể đăng công thức (nếu chọn User).

## Cấu trúc thư mục chính

```
src/main/java/com/recipeshare/
  config/          # Security, WebMvc, DataLoader
  entity/          # Chef, Recipe, Role, RecipeStatus
  repository/      # ChefRepository, RecipeRepository
  service/         # ChefService, RecipeService, ImageProcessingService
  controller/      # Home, Auth, Recipe, Admin
  security/        # JWT, UserDetails
  dto/
src/main/resources/
  templates/       # Thymeleaf (index, login, register, recipes/*, admin/*)
  static/css/      # style.css (theme vàng, nhà bếp)
  application.yml
```

## Flow chính

1. **Viewer**: Vào trang chủ / Công thức → Xem danh sách và chi tiết công thức (đã duyệt).
2. **User**: Đăng nhập → Đăng công thức (ảnh bìa, nguyên liệu, bước làm) → Công thức ở trạng thái **Chờ duyệt** trong "Công thức của tôi".
3. **Admin**: Đăng nhập → Vào **Quản trị** → Duyệt hoặc Từ chối từng công thức → Công thức **Đã duyệt** hiển thị công khai trên trang chủ và trang Công thức.
