/**
 * Created by Kai on 24/01/2018.
 */

function addParamHeuristique(){
    const paramHeu = "<div></br></br>" +
        "<label for='nomsParam[]'>Nom : </label>" +
        "<input type='text' name='nomsParam[]' id='nomsParam[]'/> " +
        "<label for='typesParam[]'>Type : </label>" +
        "<input type='text' name='typesParam[]' id='typesParam[]'/> " + 
        "<input class='deleteParam' type='button' value='Supprimer'/></div>";
    $('#paramNec').append(paramHeu);
}

$(document).on("click", ".deleteParam", function(){
    $(this).closest("div").remove();
});
