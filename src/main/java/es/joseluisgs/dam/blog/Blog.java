package es.joseluisgs.dam.blog;

import es.joseluisgs.dam.blog.controller.*;
import es.joseluisgs.dam.blog.dao.Category;
import es.joseluisgs.dam.blog.dao.Comment;
import es.joseluisgs.dam.blog.dao.Post;
import es.joseluisgs.dam.blog.dao.User;
import es.joseluisgs.dam.blog.dto.CategoryDTO;
import es.joseluisgs.dam.blog.dto.CommentDTO;
import es.joseluisgs.dam.blog.dto.PostDTO;
import es.joseluisgs.dam.blog.dto.UserDTO;
import es.joseluisgs.dam.blog.manager.HibernateController;
import es.joseluisgs.dam.blog.mapper.CategoryMapper;
import es.joseluisgs.dam.blog.mapper.PostMapper;
import es.joseluisgs.dam.blog.mapper.UserMapper;
import es.joseluisgs.dam.blog.repository.CommentRepository;
import es.joseluisgs.dam.blog.service.CommentService;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

public class Blog {
    private static Blog instance;

    private Blog() {
    }

    public static Blog getInstance() {
        if (instance == null) {
            instance = new Blog();
        }
        return instance;
    }

    public void initData() {
        HibernateController hc = HibernateController.getInstance();
        hc.open();
        // Categorías
        System.out.println("Insertando Categorias de Ejemplo");
        hc.getTransaction().begin();
        // Borro todas las categorías antes
        hc.getManager().createNamedQuery("Category.findAll", Category.class).getResultList().forEach(c -> {
            hc.getManager().remove(c);
        });

        Category c1 = new Category("General"); // 1
        Category c2 = new Category("Dudas");  // 2
        Category c3 = new Category("Evaluación"); // 3
        Category c4 = new Category("Pruebas"); // 4

        hc.getManager().persist(c1);
        hc.getManager().persist(c2);
        hc.getManager().persist(c3);
        hc.getManager().persist(c4);

        hc.getTransaction().commit();

        // Usuarios
        System.out.println("Insertando Usuarios de Ejemplo");
        hc.getTransaction().begin();
        // Borro todos los usuarios antes
        hc.getManager().createNamedQuery("User.findAll", User.class).getResultList().forEach(u -> {
            hc.getManager().remove(u);
        });

        User u1 = new User("Pepe Perez","pepe@pepe.es","1234"); // 5
        User u2 = new User("Ana Anaya","ana@anaya.es","1234"); // 6
        User u3 = new User("Paco Perez","paco@perez.es","1234"); // 7
        User u4 = new User("Son Goku","goku@dragonball.es","1234"); // 8
        User u5 = new User("Chuck Norris","chuck@norris.es","1234");  // 9

        hc.getManager().persist(u1);
        hc.getManager().persist(u2);
        hc.getManager().persist(u3);
        hc.getManager().persist(u4);
        hc.getManager().persist(u5);

        hc.getTransaction().commit();

        // Post
        System.out.println("Insertando Post de Ejemplo");
        hc.getTransaction().begin();
        // Borro todos los post antes
        hc.getManager().createNamedQuery("Post.findAll", Post.class).getResultList().forEach(p -> {
            hc.getManager().remove(p);
        });

        Post p1 = new Post("Post num 1", "http://post1.com", "Este es el post num 1", u1, c1); //10
        Post p2 = new Post("Post num 2", "http://post2.com", "Este es el post num 1", u2, c2); //11
        Post p3 = new Post("Post num 3", "http://post3.com", "Este es el post num 1", u3, c3); //12
        Post p4 = new Post("Post num 4", "http://post4.com", "Este es el post num 1", u1, c1); //13
        Post p5 = new Post("Post num 5", "http://post5.com", "Este es el post num 1", u2, c3); //14

        hc.getManager().persist(p1);
        hc.getManager().persist(p2);
        hc.getManager().persist(p3);
        hc.getManager().persist(p4);
        hc.getManager().persist(p5);

        hc.getTransaction().commit();

        // Comentarios
        System.out.println("Insertando Comentarios de Ejemplo");
        hc.getTransaction().begin();
        // Borro todos los post antes
        hc.getManager().createNamedQuery("Comment.findAll", Comment.class).getResultList().forEach(c -> {
            hc.getManager().remove(c);
        });

        Comment cm1 = new Comment("Comentario 01,", u1, p1);//15
        Comment cm2 = new Comment("Comentario 02,", u2, p2);//16
        Comment cm3 = new Comment("Comentario 03,", u3, p2);//17
        Comment cm4 = new Comment("Comentario 04,", u1, p3);//18
        Comment cm5 = new Comment("Comentario 05,", u4, p4);//19
        Comment cm6 = new Comment("Comentario 06,", u1, p3);//20
        Comment cm7 = new Comment("Comentario 07,", u4, p4);//21
        Comment cm8 = new Comment("Comentario 08,", u2, p3);//22

        hc.getManager().persist(cm1);
        hc.getManager().persist(cm2);
        hc.getManager().persist(cm3);
        hc.getManager().persist(cm4);
        hc.getManager().persist(cm5);
        hc.getManager().persist(cm6);
        hc.getManager().persist(cm7);
        hc.getManager().persist(cm8);

        hc.getTransaction().commit();

        hc.close();

    }

    public void Categories() {
        System.out.println("INICIO CATEGORIAS");

        CategoryController categoryController = CategoryController.getInstance();

        // Iniciamos las categorias

        System.out.println("GET Todas las categorías");
        System.out.println(categoryController.getAllCategories());

        System.out.println("GET Categoría con ID = 2"); // Mira en el explorador a ver que categoría hay
        System.out.println(categoryController.getCategoryById(2L));

        // id: 10
        System.out.println("POST Categoría");
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .texto("Insert " + LocalDateTime.now())
                .build();
        System.out.println(categoryController.postCategory(categoryDTO));

        // id: 11
        categoryDTO = CategoryDTO.builder()
                .texto("Insert Otra " + LocalDateTime.now())
                .build();
        System.out.println(categoryController.postCategory(categoryDTO));

        System.out.println("UPDATE Categoría con ID 10");
        Optional<CategoryDTO> optionalCategoryDTO = categoryController.getCategoryByIdOptional(10L);
        if (optionalCategoryDTO.isPresent()) {
            optionalCategoryDTO.get().setTexto("Update " + LocalDateTime.now());
            System.out.println(categoryController.updateCategory(optionalCategoryDTO.get()));
        }

        System.out.println("DELETE Categoría con ID 11");
        optionalCategoryDTO = categoryController.getCategoryByIdOptional(11L);
        if (optionalCategoryDTO.isPresent()) {
            System.out.println(optionalCategoryDTO.get());
            System.out.println(categoryController.deleteCategory(optionalCategoryDTO.get()));
        }

        System.out.println("FIN CATEGORIAS");
    }

    public void Users() {
        System.out.println("INICIO USUARIOS");

        UserController userController = UserController.getInstance();

        System.out.println("GET Todos los usuarios");
        System.out.println(userController.getAllUsers());

        System.out.println("GET Usuario con ID = 5");
        System.out.println(userController.getUserById(5L));

        // id 12
        System.out.println("POST nuevo Usuario 1");
        UserDTO userDTO = UserDTO.builder()
                .nombre("Insert " + LocalDateTime.now())
                .email("email" + LocalDateTime.now() + "@mail.com")
                .password("1234")
                .build();
        System.out.println(userController.postUser(userDTO));

        // ide 13
        System.out.println("POST nuevo Usuario 2");
        userDTO = UserDTO.builder()
                .nombre("Insert Otro" + LocalDateTime.now())
                .email("emailOtro" + LocalDateTime.now() + "@mail.com")
                .password("1234")
                .build();
        System.out.println(userController.postUser(userDTO));

        System.out.println("UPDATE Usuario con ID 12");
        Optional<UserDTO> optionalUserDTO = userController.getUserByIdOptional(12L);
        if (optionalUserDTO.isPresent()) {
            optionalUserDTO.get().setNombre("Update " + LocalDateTime.now());
            optionalUserDTO.get().setEmail("emailUpdate" + LocalDateTime.now() + "@mail.com");
            System.out.println(userController.updateUser(optionalUserDTO.get()));
        }

        System.out.println("DELETE Usuario con ID 13");
        optionalUserDTO = userController.getUserByIdOptional(13L);
        if (optionalUserDTO.isPresent()) {
            System.out.println(userController.deleteUser(optionalUserDTO.get()));
        }

        System.out.println("FIN USUARIOS");
    }



    public void Login() {
        System.out.println("INICIO LOGIN");

        LoginController loginController = LoginController.getInstance();

        System.out.println("Login con un usario que SI existe");
        System.out.println(loginController.login("pepe@pepe.es", "1234"));

        System.out.println("Login con un usario que SI existe Y mal Password datos correctos");
        System.out.println(loginController.login("pepe@pepe.es", "1255"));

        System.out.println("Login con un usario que NO existe o mal Password datos correctos");
        System.out.println(loginController.login("pepe@pepe.com", "1255"));

        System.out.println("Logout de usuario que está logueado");
        System.out.println(loginController.logout(5L)); // Mirar su ID

        System.out.println("Logout de usuario que no está logueado");
        System.out.println(loginController.logout(999L));

        System.out.println("FIN LOGIN");
    }

    /*
    public void Posts() {
        System.out.println("INICIO POSTS");

        PostController postController = PostController.getInstance();

        System.out.println("GET Todos los Post");
        System.out.println(postController.getAllPost());

        System.out.println("GET Post con ID = 2");
        System.out.println(postController.getPostById(2L));

        System.out.println("POST Insertando Post 1");
        // Lo primero que necesito es un usuario...
        UserController userController = UserController.getInstance();
        UserDTO user = userController.getUserByIdOptional(1L).get(); // Sé que el id existe ...
        // Y una categoría
        CategoryController categoryController = CategoryController.getInstance();
        CategoryDTO category = categoryController.getCategoryByIdOptional(1L).get();

        // Neceistamos mapearlos a objetos y no DTO, no debería ser así y trabajar con DTO completos, pero no es tan crucial para el CRUD
        UserMapper userMapper = new UserMapper();
        CategoryMapper categoryMapper = new CategoryMapper();

        System.out.println("POST Insertando Post 2");
        PostDTO postDTO = PostDTO.builder()
                .titulo("Insert " + LocalDateTime.now())
                .contenido("Contenido " + Instant.now().toString())
                .url("http://" + Math.random() + ".dominio.com")
                .user(userMapper.fromDTO(user))
                .category(categoryMapper.fromDTO(category))
                .build();

        System.out.println(postController.postPost(postDTO));
        user = userController.getUserByIdOptional(1L).get();
        category = categoryController.getCategoryByIdOptional(1L).get();
        postDTO = PostDTO.builder()
                .titulo("Insert Otro" + LocalDateTime.now())
                .contenido("Contenido Otro" + Instant.now().toString())
                .url("http://" + Math.random() + ".dominio.com")
                .user(userMapper.fromDTO(user))
                .category(categoryMapper.fromDTO(category))
                .build();
        System.out.println(postController.postPost(postDTO));

        System.out.println("UPDATE Post con ID 5");
        Optional<PostDTO> optionalPostDTO = postController.getPostByIdOptional(5L);
        if (optionalPostDTO.isPresent()) {
            optionalPostDTO.get().setTitulo("Update " + LocalDateTime.now());
            optionalPostDTO.get().setContenido("emailUpdate" + LocalDateTime.now() + "@mail.com");
            System.out.println(postController.updatePost(optionalPostDTO.get()));
        }

        System.out.println("DELETE Post con ID 6");
        optionalPostDTO = postController.getPostByIdOptional(6L);
        if (optionalPostDTO.isPresent()) {
            System.out.println(postController.deletePost(optionalPostDTO.get()));
        }

        System.out.println("GET By Post con User ID 1 usando la Relación Post --> Usuario");
        postController.getPostByUserId(1L).forEach(System.out::println);

        System.out.println("GET By Post con User ID 1 usando la Relación Usuario --> Post");
        user = userController.getUserByIdOptional(1L).get();
        // Por cierto, prueba quitando el FetchType.EAGER de getPost de User y mira que pasa. ¿Lo entiendes?
        user.getPosts().forEach(System.out::println);

        System.out.println("FIN POSTS");
    }

    public void Comments() {
        System.out.println("INICIO COMENTARIOS");

        CommentController commentController = CommentController.getInstance();

        System.out.println("GET Todos los Comentarios");
        System.out.println(commentController.getAllComments());

        System.out.println("GET Comentario con ID = 2");
        System.out.println(commentController.getCommentById(2L));

        System.out.println("POST Insertando Comentario 1");

        UserController userController = UserController.getInstance();
        UserDTO user = userController.getUserByIdOptional(1L).get(); // Sé que el id existe ...
        // Y un Post
        PostController postController = PostController.getInstance();
        PostDTO post = postController.getPostByIdOptional(1L).get();

        // Neceistamos mapearlos a objetos y no DTO, no debería ser así y trabajar con DTO completos, pero no es tan crucial para el CRUD
        UserMapper userMapper = new UserMapper();
        PostMapper postMapper = new PostMapper();
        CommentDTO commentDTO = CommentDTO.builder()
                .texto("Comentario " + Instant.now().toString())
                .user(userMapper.fromDTO(user))
                .post(postMapper.fromDTO(post))
                .build();
        System.out.println(commentController.postComment(commentDTO));

        System.out.println("POST Insertando Comentario 2");

        userController = UserController.getInstance();
        user = userController.getUserByIdOptional(1L).get(); // Sé que el id existe ...
        // Y un Post
        postController = PostController.getInstance();
        post = postController.getPostByIdOptional(3L).get();

        // Neceistamos mapearlos a objetos y no DTO, no debería ser así y trabajar con DTO completos, pero no es tan crucial para el CRUD
        userMapper = new UserMapper();
        postMapper = new PostMapper();
        commentDTO = CommentDTO.builder()
                .texto("Comentario " + Instant.now().toString())
                .user(userMapper.fromDTO(user))
                .post(postMapper.fromDTO(post))
                .build();
        System.out.println(commentController.postComment(commentDTO));

        System.out.println("UPDATE Comentario con ID 5");
        Optional<CommentDTO> optionalCommentDTO = commentController.getCommentByIdOptional(6L);
        if (optionalCommentDTO.isPresent()) {
            optionalCommentDTO.get().setTexto("Update " + LocalDateTime.now());
            System.out.println(commentController.updateComment(optionalCommentDTO.get()));
        }

        System.out.println("DELETE Comentario con ID 7");
        optionalCommentDTO = commentController.getCommentByIdOptional(7L);
        if (optionalCommentDTO.isPresent()) {
            System.out.println(commentController.deleteComment(optionalCommentDTO.get()));
        }

        System.out.println("GET Dado un Post Obtener sus Comentarios Post --> Comentarios");
        postController.getPostById(2L).getComments().forEach(System.out::println);

        System.out.println("GET Dado un usuario obtener sus comentarios Usuario --> Comentarios");
        userController.getUserById(1L).getComentarios().forEach(System.out::println);

        System.out.println("GET Dado un comentario saber su Post Comentario --> Post");
        System.out.println(commentController.getCommentById(2L).getPost());

        System.out.println("GET Dado un comentario saber su Autor Comentario --> Comentario");
        System.out.println(commentController.getCommentById(2L).getUser());

        System.out.println("DELETE Borrrando un post se borran sus comentarios? Post --> Comentario"); // Cascada
        Optional<PostDTO> optionalPostDTO = postController.getPostByIdOptional(4L);
        if (optionalPostDTO.isPresent()) {
            System.out.println(postController.deletePost(optionalPostDTO.get()));
        }

        System.out.println("DELETE Borrrando un usuario se borran comentarios User --> Comentarios"); // Cascada
        // No, porque no puedo borrar un usuario si tiene un post.
        Optional<UserDTO> optionalUserDTO = userController.getUserByIdOptional(1L);
        if (optionalUserDTO.isPresent()) {
            System.out.println(userController.deleteUser(optionalUserDTO.get()));
        }

        System.out.println("FIN COMENTARIOS");
    }

    */
}
