/**
 * Created by Kai on 24/01/2018.
 */

function info(id) {
    $.get('/GraphMatching/DetailAppareillement?execution=' + id
        +'&methode='+ $("#methode").val()
        +'&G1=' + $("#G1").val()
        +'&G2=' + $("#G2").val(),
        function(responseText){
            document.getElementById('resultat').innerHTML = responseText;
            $('.noeuds').dataTable(
                {
                    "bFilter": true,
                    "sDom": '<ti>',
                    "scrollY": "400px",
                    "scrollCollapse": true,
                    "paging": false
                }

            );
            $('.arcs').dataTable(
                {
                    "bFilter": true,
                    "sDom": '<ti>',
                    "scrollY": "400px",
                    "scrollCollapse": true,
                    "paging": false
                }
            );
        }
    );

}