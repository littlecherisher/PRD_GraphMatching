/**
 * Created by Kai on 24/01/2018.
 */

function addParamHeuristique(){
    const paramHeu = "<div></br></br>" +
        "<label for='nomsParamHeu[]'>Nom : </label>" +
        "<input type='text' name='nomsParamHeu[]' id='nomsParamHeu[]'/> " +
        "&nbsp<label for='typesParamHeu[]'>Type : </label>" +
    	"<select name='typesParamHeu[]' id='typesParamHeu[]' style='width:100px;font-size:15px' size='1' value='1'>"+
    			"<option value='int' >int</option>" +
    			"<option value='float' >float</option>" +
    			"<option value='double' >double</option>" +
    	"</select>" +
        "&nbsp&nbsp<input class='deleteParam' type='button' value='Supprimer'/></div>";
    $('#paramNec').append(paramHeu);
}

$(document).on("click", ".deleteParam", function(){
    $(this).closest("div").remove();
});
