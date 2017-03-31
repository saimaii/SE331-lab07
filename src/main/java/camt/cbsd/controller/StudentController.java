package camt.cbsd.controller;

import camt.cbsd.entity.Student;
import camt.cbsd.services.StudentService;
import camt.cbsd.services.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
@ConfigurationProperties(prefix="server")
@Component
@Path("/student")
public class StudentController {
    StudentService studentService;
    private String imageServerDir;

    public void setImageServerDir(String imageServerDir) {
        this.imageServerDir = imageServerDir;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadOnlyStudent(Student student){
        System.out.println(student);
        return Response.ok().entity(student).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudents(){
        List<Student> students = studentService.getStudents();
        return Response.ok(students).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") long id){
        Student student = studentService.findById(id);
        if (student != null)
            return Response.ok(student).build();
        else
            //http code 204
            return Response.status(Response.Status.NO_CONTENT).build();

    }
    @GET
    @Path("/images/{fileName}")
    @Produces({"image/png","image/jpg","image/gif"})
    public Response getStudentImage(@PathParam("fileName") String filename){
        File file = Paths.get(imageServerDir+filename).toFile();
        Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
        if (file.exists()){
            responseBuilder.header("Content-Disposition", "attachment;filename=\"a.jpg\"");
        }else{return Response.status(Response.Status.NOT_FOUND).build();}

        return responseBuilder.build();

    }

}
