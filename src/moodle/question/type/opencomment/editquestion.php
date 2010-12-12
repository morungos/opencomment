<?php
    global $QTYPES;
    $QTYPES[$question->qtype]->get_question_options($question);
    
    print_heading_with_help(get_string("editingopencomment", "opencomment"), "opencomment", "opencomment");
    require("$CFG->dirroot/question/type/opencomment/editquestion.html");
?>
