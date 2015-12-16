package se.travappar.api.utils.publish.mailchimp;

import com.ecwid.mailchimp.MailChimpObject;

import java.util.List;

public class GetSegmentsResult extends MailChimpObject {
    @Field(name = "static")
    public List<Segment> staticList;

    @Field
    public List<Segment> saved;

    @Field
    public String created_date;//the date+time the segment was created

    @Field
    public String last_update;//the date+time the segment was last updated (add or del)

}
