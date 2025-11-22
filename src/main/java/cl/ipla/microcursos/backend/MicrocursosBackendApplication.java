package cl.ipla.microcursos.backend;

import cl.ipla.microcursos.backend.model.*;
import cl.ipla.microcursos.backend.mongo.model.Acta;
import cl.ipla.microcursos.backend.mongo.model.Rubrica;
import cl.ipla.microcursos.backend.mongo.repository.ActaRepository;
import cl.ipla.microcursos.backend.mongo.repository.RubricaRepository;
import cl.ipla.microcursos.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class MicrocursosBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrocursosBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            CursoRepository cursoRepository,
            ModuloRepository moduloRepository,
            InscripcionRepository inscripcionRepository,
            ResultadoRepository resultadoRepository,
            RubricaRepository rubricaRepository,
            ActaRepository actaRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {
            System.out.println("===== Paso 1: inicializando dominio =====");

            // Roles
            Rol adminRole = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> rolRepository.save(Rol.builder().nombre("ADMIN").build()));

            Rol estudianteRole = rolRepository.findByNombre("ESTUDIANTE")
                    .orElseGet(() -> rolRepository.save(Rol.builder().nombre("ESTUDIANTE").build()));

            // Usuario estudiante demo
            Usuario estudiante = usuarioRepository.findByEmail("estudiante@demo.cl")
                    .orElseGet(() -> usuarioRepository.save(
                            Usuario.builder()
                                    .nombre("Estudiante Demo")
                                    .email("estudiante@demo.cl")
                                    .passwordHash(passwordEncoder.encode("Estudiante123"))
                                    .rol(estudianteRole)
                                    .build()
                    ));

            // Usuario admin demo
            usuarioRepository.findByEmail("admin@demo.cl")
                    .orElseGet(() -> usuarioRepository.save(
                            Usuario.builder()
                                    .nombre("Admin Demo")
                                    .email("admin@demo.cl")
                                    .passwordHash(passwordEncoder.encode("Admin123"))
                                    .rol(adminRole)
                                    .build()
                    ));

            // Curso demo
            Curso curso = cursoRepository.findAll().stream().findFirst()
                    .orElseGet(() -> cursoRepository.save(
                            Curso.builder()
                                    .titulo("Introducción a Microcursos")
                                    .descripcion("Curso de ejemplo para el proyecto de título.")
                                    .fechaCreacion(LocalDateTime.now())
                                    .build()
                    ));

            // Módulos demo si no hay
            if (moduloRepository.count() == 0) {
                moduloRepository.save(
                        Modulo.builder()
                                .titulo("Módulo 1: Bienvenida")
                                .descripcion("Introducción al curso.")
                                .orden(1)
                                .curso(curso)
                                .build()
                );
                moduloRepository.save(
                        Modulo.builder()
                                .titulo("Módulo 2: Conceptos básicos")
                                .descripcion("Contenido base.")
                                .orden(2)
                                .curso(curso)
                                .build()
                );
            }

            // Inscripción demo
            Inscripcion inscripcion = inscripcionRepository.findAll().stream().findFirst()
                    .orElseGet(() -> inscripcionRepository.save(
                            Inscripcion.builder()
                                    .usuario(estudiante)
                                    .curso(curso)
                                    .fechaInscripcion(LocalDateTime.now())
                                    .estado("ACTIVA")
                                    .progreso(0)
                                    .build()
                    ));

            // Resultado demo
            if (resultadoRepository.count() == 0) {
                resultadoRepository.save(
                        Resultado.builder()
                                .inscripcion(inscripcion)
                                .modulo(null) // por ahora general
                                .puntaje(100.0)
                                .fechaRegistro(LocalDateTime.now())
                                .build()
                );
            }

            // Mongo opcional
            try {
                // Rúbrica en Mongo
                if (rubricaRepository.count() == 0) {
                    rubricaRepository.save(
                            Rubrica.builder()
                                    .nombre("Rúbrica de Microcursos")
                                    .descripcion("Rúbrica de ejemplo para el curso demo.")
                                    .cursoId(curso.getId())
                                    .build()
                    );
                }

                // Acta en Mongo
                if (actaRepository.count() == 0) {
                    actaRepository.save(
                            Acta.builder()
                                    .inscripcionId(inscripcion.getId())
                                    .notaFinal(7.0)
                                    .observaciones("Acta de ejemplo.")
                                    .fechaEmision(LocalDateTime.now())
                                    .build()
                    );
                }

                System.out.println("===== Resumen MongoDB =====");
                System.out.println("Rúbricas: " + rubricaRepository.count());
                System.out.println("Actas: " + actaRepository.count());
            } catch (Exception e) {
                System.out.println("MongoDB no disponible, se omiten datos de rúbricas/actas: " + e.getMessage());
            }

            System.out.println("===== Resumen BD =====");
            System.out.println("Roles: " + rolRepository.count());
            System.out.println("Usuarios: " + usuarioRepository.count());
            System.out.println("Cursos: " + cursoRepository.count());
            System.out.println("Módulos: " + moduloRepository.count());
            System.out.println("Inscripciones: " + inscripcionRepository.count());
            System.out.println("Resultados: " + resultadoRepository.count());
        };
    }
}
