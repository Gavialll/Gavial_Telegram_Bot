package com.bot.gavial_bot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class IrregularVerb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String past;
    private String present;
    private String future;
    private String ukraine;

    @Override
    public String toString() {
        return present.replace("to ", "").trim() + " " + past + " " + future;
    }

    public String getPresent() {
        return present.replace("to ", "").trim();
    }
}
