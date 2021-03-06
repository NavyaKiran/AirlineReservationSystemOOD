package com.example.OODProject.Service;

import com.example.OODProject.Exception.NotFoundException;
import com.example.OODProject.Model.Schedule;
import com.example.OODProject.Model.UserRoles;
import com.example.OODProject.Model.Users;
import com.example.OODProject.Request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.OODProject.DataAccess.users_dataaccess;
import com.example.OODProject.Exception.AvailableRecordException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class user_services_implementation implements user_services {

    @Autowired
    users_dataaccess userObj;


    @Override
    public ResponseEntity<?> create_user(UserRequest request) {
        try {
            Optional<Users> findByUserID = userObj.findById(request.getEmail());
            if(findByUserID.isPresent())
            {
                throw new Exception("User with email ID "+request.getEmail()+" is already present");
            }
            else {
                Users user = new Users();
                user.setRole(UserRoles.USER);
                user.setEmail(request.getEmail());
                user.setUsername(request.getUsername());
                user.setPassword(request.getPassword());
                user.setPhone_number(request.getPhone_number());
                userObj.save(user);

                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }
        }
        catch(Exception exception)
        {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<?> findByEmail(String email) {
        Optional<Users> findUsingID = userObj.findById(email);
        try {
            if (findUsingID.isPresent()) {
                Users userfound = findUsingID.get();
                return new ResponseEntity<Users>(userfound, HttpStatus.OK);
            } else {
                throw new NotFoundException("User with email " +email+ " not found");
            }
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?>  update(Users updateuser, String email) {
        try {
            Users user = userObj.findById(email).get();
            if (user == null)
                throw new NotFoundException("The user with email " + email + " has not been found");

            if (updateuser.getUsername() != null) {
                user.setUsername(updateuser.getUsername());
            }

            if (!Objects.isNull(updateuser.getPhone_number()) && updateuser.getPhone_number() != 0) {
                user.setPhone_number(updateuser.getPhone_number());
            }
//        if(updateuser.getRole() != user.getRole())
//        {
//            user.setRole(updateuser.getRole());
//        }
            userObj.save(user);
            return new ResponseEntity<Users>(user, HttpStatus.OK);
        }
        catch(NotFoundException exception) {
        //catch(Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<?> delete(String email) {
        try {
            Optional<Users> findByEmailID = userObj.findById(email);
            if (findByEmailID.isPresent()) {
                userObj.deleteById(email);
                return new ResponseEntity<>("The user with email " + email + " has been deleted", HttpStatus.ACCEPTED);
            } else
                throw new Exception("The user with email " + email + "could not be deleted");
        }
        catch(Exception exception)
        {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> view_all() {
        try {
            List<Users> user = userObj.findAll();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    }

