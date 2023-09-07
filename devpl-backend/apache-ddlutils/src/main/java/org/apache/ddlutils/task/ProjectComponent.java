package org.apache.ddlutils.task;

public abstract class ProjectComponent implements Cloneable {
    /**
     * @deprecated
     */
    protected Project project;
    /**
     * @deprecated
     */
    protected Location location;
    /**
     * @deprecated
     */
    protected String description;

    public ProjectComponent() {
        this.location = Location.UNKNOWN_LOCATION;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void log(String msg) {
        this.log(msg, 2);
    }

    public void log(String msg, int msgLevel) {
        if (this.getProject() != null) {
            this.getProject().log(msg, msgLevel);
        } else if (msgLevel <= 2) {
            System.err.println(msg);
        }

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ProjectComponent pc = (ProjectComponent) super.clone();
        pc.setLocation(this.getLocation());
        pc.setProject(this.getProject());
        return pc;
    }
}
