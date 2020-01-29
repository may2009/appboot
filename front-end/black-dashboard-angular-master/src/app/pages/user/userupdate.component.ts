import { Component, OnInit } from "@angular/core";
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {ServicesuserService} from "../../services/servicesuser.service";
import {Userclass} from "../../class/userclass";
import {Router} from "@angular/router";
import {first} from "rxjs/operators";
import Swal from "sweetalert2";
@Component({
  selector: 'app-userupdate',
  templateUrl: 'userupdate.component.html',
  styleUrls: ['userupdate.component.scss']
})
export class UserupdateComponent implements OnInit {
  editForm: FormGroup;
  userclass:Userclass = new Userclass();
  submitted = false;

  constructor(private formBuilder: FormBuilder,private servicesuserService :ServicesuserService, private  router :Router) { }

  ngOnInit() {
    $(".menuuser").addClass("active");
    let userId = window.localStorage.getItem("editUserId");
    if(!userId) {
      this.router.navigate(['list-user']);
      return;
    }
    this.editForm = this.formBuilder.group({
      id: [''],
      name: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required],
      deleted: ['']
    });
    this.servicesuserService.getUserById(userId)
      .subscribe( data => {
        this.editForm.setValue(data);
      });
  }
  get f() { return this.editForm.controls; }
  onSubmit() {
    this.submitted = true;
    if (this.editForm.invalid) {
      return;
    }

    this.servicesuserService.updateUser(this.editForm.value)
      .pipe(first())
      .subscribe(
        data => {

          Swal.fire({
            icon: 'success',
            title: 'your work has been edited',
            showClass: {
              popup: 'animated fadeInDown faster'
            },
            hideClass: {
              popup: 'animated fadeOutUp faster'
            }
          }).then((result) => {
            if (result.value) {
              this.router.navigate(["/user"])
            }
          })
        },
        error => {
          alert(error);
        });



  }


}