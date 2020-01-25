package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public abstract class Entity implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @JsonIgnore
    protected long id;

}