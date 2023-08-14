package nl.aswad.observability.controller

import nl.aswad.observability.dto.DataAsset
import nl.aswad.observability.dto.KafkaInput
import nl.aswad.observability.resolver.DataAssetResolver
import nl.aswad.observability.resolver.KafkaResolver
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class GraphQLController(
    val kafkaResolver: KafkaResolver,
    val dataAssetResolver: DataAssetResolver
) {

    @QueryMapping
    fun dataAsset(@Argument fqn: String): Mono<DataAsset> {
        return dataAssetResolver.find(fqn)
    }

    @MutationMapping
    fun bootstrapKafka(@Argument input: KafkaInput): Mono<DataAsset> {
        return kafkaResolver.boostrap(input)
    }

    @SchemaMapping(typeName = "DataAsset", field = "tags")
    fun getAuthor(dataAsset: DataAsset): List<String> {
        return dataAsset.tags.map { index -> "tag-$index" }
    }
}
