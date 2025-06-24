package star.part01.model;

import java.util.UUID;

public class Rule{
    private UUID id;
    private String instruction;
    private String annotation;

    public Rule(UUID id, String instruction, String annotation) {
        this.id = id;
        this.instruction = instruction;
        this.annotation = annotation;
    }

    public UUID getId() {
        return id;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setId(UUID id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", Instruction='" + instruction + '\'' +
                ", annotation='" + annotation + '\'' +
                '}';
    }
}
