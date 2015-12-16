package se.travappar.api.utils.publish.mailchimp;

import com.ecwid.mailchimp.MailChimpObject;

public class Segment extends MailChimpObject {
    @Field
    public Integer id;//the id of the segment
    @Field
    public String name;//the name for the segment
    @Field
    public String created_date;//the date+time the segment was created
    @Field
    public String last_update;//the date+time the segment was last updated (add or del)
    @Field
    public String last_reset;//
    @Field
    public String segment_opts;//same match+conditions struct typically used
    @Field
    public String segment_text;//
}
