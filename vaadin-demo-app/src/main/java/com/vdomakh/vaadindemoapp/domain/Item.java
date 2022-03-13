package com.vdomakh.vaadindemoapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties
public class Item {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String description;
    private Boolean end;

    public Item(Long id, String name, String email, String phone, String description, Boolean end) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.description = description;
        this.end = end;
    }

    public Item () {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
	public Boolean getEnd() {
		return end;
	}
	
	
	public void setEnd(Boolean end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", description="
				+ description + "]";
	}
    
}
