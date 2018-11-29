package com.id1212.hw3.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@NamedQueries({
    @NamedQuery(
	name = "findAllFiles",
	query = "SELECT f FROM CatalogFile f"
    ),
    @NamedQuery(
    name = "findFileByOwner",
    query = "SELECT f FROM CatalogFile f WHERE f.owner = :owner"
    ),
    @NamedQuery(
    name = "findFileByName",
    query = "SELECT f FROM CatalogFile f WHERE f.name = :name"
    )
})

@Entity(name="CatalogFile")
public class CatalogFile {

    @Id
    @Column(name="name", nullable=false)
    private String name;

    @Column(name="size", nullable=false)
    private long size;

    @Column(name="owner", nullable=false)
    private String owner;

    @Column(name="isPublicReadable", nullable=false)
    private boolean isPublicReadable;

    @Column(name="isPublicWritable", nullable=false)
    private boolean isPublicWritable;

    // Contructor
    public CatalogFile(String name, long size, String owner, boolean isPublicReadable, boolean isPublicWritable) {
        this.name = name;
        this.size = size;
        this.owner = owner;
        this.isPublicReadable = isPublicReadable;
        this.isPublicWritable = isPublicWritable;
    }

    // default constructor
    public CatalogFile(){}

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * @return the isPublicReadable
     */
    public boolean isPublicReadable() {
        return isPublicReadable;
    }

    /**
     * @return the isPublicWritable
     */
    public boolean isPublicWritable() {
        return isPublicWritable;
    }

    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }
}