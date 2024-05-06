package model;

public class Employee {

    private int id;
    private String nameSurname;
    private String profilePhotoPath;
    private String departmentName;

    public Employee() {
    }

    public Employee(int id, String nameSurname, String profilePhotoPath, String departmentName) {
        this.id = id;
        this.nameSurname = nameSurname;
        this.profilePhotoPath = profilePhotoPath;
        this.departmentName = departmentName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
