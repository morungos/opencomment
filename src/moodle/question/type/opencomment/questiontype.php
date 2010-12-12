<?php

/**
 * Open Comment question type for Moodle
 * 
 * @copyright &copy; 2006 The Robert Gordon University
 * @author Stuart Watt
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * 
 * This implements a free text question designed only for formative
 * use. It allows learners to type in a free text response to a 
 * question, which will then be despatched to a web service for
 * analysis. The web service can then return some HTML text to
 * provide feedback to the learner.
 * 
 * NOTE: Currently, question types do not automatically implement
 * database corrections or upgrades. This needs to be done manually
 * at present. 
 */
 
class question_opencomment_qtype extends default_questiontype {

    function name() {
        return 'opencomment';
    }
    
    function get_question_options(&$question) {
        if (!$question->options = get_record('question_opencomment', 'question', $question->id)) {
            notify('Error: Missing question options!');
            return false;
        }
        
        return true;
    }
    
    function save_question_options($question) {
    	global $result;
        if ($options = get_record("question_opencomment", "question", $question->id)) {
            $options->url = $question->url;
            $options->feedback = $question->feedback;
            if (!update_record("question_opencomment", $options)) {
                $result->error = "Could not update quiz OpenComment options! (id=$options->id)";
                return $result;
            }
        } else {
            unset($options);
            $options->question = $question->id;
            $options->url = $question->url;
            $options->feedback = $question->feedback;
            if (!insert_record("question_opencomment", $options)) {
                $result->error = "Could not insert quiz OpenComment options!";
                return $result;
            }
        }
        return true;
    }

    /**
    * Prints the submit button(s) for the question in the given state
    *
    * This function prints the submit button(s) for the question in the
    * given state. The name of any button created will be prefixed with the
    * unique prefix for the question in $question->name_prefix. The suffix
    * 'submit' is reserved for the single question submit button and the suffix
    * 'validate' is reserved for the single question validate button (for
    * question types which support it). Other suffixes will result in a response
    * of that name in $state->responses which the printing and grading methods
    * can then use.
    * @param object $question The question for which the submit button(s) are to
    *                         be rendered. Question type specific information is
    *                         included. The name prefix for any
    *                         named elements is in ->name_prefix.
    * @param object $state    The state to render the buttons for. The
    *                         question type specific information is also
    *                         included.
    * @param object $cmoptions
    * @param object $options  An object describing the rendering options.
    */
    function print_question_submit_buttons(&$question, &$state, $cmoptions, $options) {
        global $USER, $CFG;

        if (!$options->readonly) {
            // Add the More Help button to provide additional guidance
            echo '<input type="submit" name="';
            echo $question->name_prefix;
            echo 'help" value="';
            print_string('morehelp', 'opencomment');
            echo '" class="submit btn"';
            echo ' />';
        }
    }
    
    function get_response($question, $state) {
        $next = $state->event;
        $url = $question->options->url;
        // Get number for the next or unfinished attempt
        $attempt = get_record('quiz_attempts', 'id', $state->attempt);
        if ($state->event === QUESTION_EVENTSAVE) {
            return $this->get_more_help($question, $state, $url, $attempt->attempt);
        } else {
            return $this->get_feedback($question, $state, $url, $attempt->attempt);
        }
    }
    
    function get_feedback($question, $state, $url, $newattempts) {
        // Return no feedback in the event if the question being
        // opened for the first time
        if ($state->event == QUESTION_EVENTOPEN) {
            return "";
        }
        
        $text = $state->responses[''];
        $attempts = $newattempts;
        $name = $question->name;
        
        $wsdlfile = dirname(realpath(__FILE__)) . "/OpenComment.wsdl";
        
        $client = new SoapClient($wsdlfile);
        $client->initializeEngine();
        $feedback = $client->getFeedback($name, $url, $text, $attempts);
  
        return $feedback;
    }
    
    function get_more_help($question, $state, $url, $attempts) {
        return $question->options->feedback;
    }
    
    function print_question_formulation_and_controls(&$question, &$state, $cmoptions, $options) {
        global $CFG;
        
        // Turn off adaptive mode, i.e., make sure each attempt 
        // is closed before providing feedback
        $cmoptions->optionflags = 0;

        $answers       = &$question->options->answers;
        $readonly      = empty($options->readonly) ? '' : 'disabled="disabled"';
        $usehtmleditor = can_use_html_editor();
        
        $formatoptions          = new stdClass;
        $formatoptions->noclean = true;
        $formatoptions->para    = false;
        
        $inputname = $question->name_prefix;
        $stranswer = get_string("answer", "quiz").': ';
        
        /// set question text and media
        $questiontext = format_text($question->questiontext,
                                   $question->questiontextformat,
                                   $formatoptions, $cmoptions->course);
                         
        $image = get_question_image($question, $cmoptions->course);

        // get response value
        if (isset($state->responses[''])) {
            $value = stripslashes_safe($state->responses['']);            
        } else {
            $value = "";
        }
        
        // answer
        if (empty($options->readonly)) {    
            // the student needs to type in their answer so print out a text editor
            $answer = print_textarea($usehtmleditor, 18, 80, 630, 400, $inputname, $value, $cmoptions->course, true);
        } else {
            // it is read only, so just format the students answer and output it
            $answer = format_text($value, $question->questiontextformat,
                                  $formatoptions, $cmoptions->course);
        }
        
        $feedback = $this->get_response($question, $state);
        
        include("$CFG->dirroot/question/type/opencomment/display.html");

        if ($usehtmleditor) {
            use_html_editor($inputname);
        }
    }

    function grade_responses(&$question, &$state, $cmoptions) {
        clean_param($state->responses[''], PARAM_CLEANHTML);
        
        $state->raw_grade = 0;
        $state->penalty = 0;
        
        $feedback = $this->get_response($question, $state);
        $state->options->feedback = $feedback;

        return true;
    }
}    
//// END OF CLASS ////

//////////////////////////////////////////////////////////////////////////
//// INITIATION - Without this line the question type is not in use... ///
//////////////////////////////////////////////////////////////////////////
$QTYPES['opencomment'] = new question_opencomment_qtype();
// The following adds the questiontype to the menu of types shown to teachers
$QTYPE_MENU['opencomment'] = get_string("opencomment", "opencomment");
// Add opencomment to the list of manually graded questions
//$QTYPE_MANUAL = isset($QTYPE_MANUAL) ? $QTYPE_MANUAL.",'opencomment'" : "'opencomment'";

?>
