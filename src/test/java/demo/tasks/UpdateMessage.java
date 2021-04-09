package demo.tasks;

import demo.dto.MessageDto;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;

public class UpdateMessage {
    public static Performable withAuthorAndMessage(MessageDto messageDto) {
        return Task.where(
                "{0} attempts to update message",
                Put.to("/taqelah/messages/2")
                .with(request -> {
                    request.body(messageDto);
                    request.header("Content-Type", "application/json");
                    return request;
            }));
    
    }
}


